package id.fireguard.vmm;

import static java.lang.String.format;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import id.fireguard.Settings;
import id.fireguard.vmm.vmconfig.VmConfig;
import id.fireguard.vmm.vmconfig.VmConfigUtils;
import id.xfunction.Exec;
import id.xfunction.function.Unchecked;

public class VirtualMachineManager {

	private Settings settings;
	private VirtualMachinesStore manager;
	private VirtualMachineBuilder builder = new VirtualMachineBuilder();
	private VmConfigUtils configUtils = new VmConfigUtils();

	private VirtualMachineManager(Settings settings, VirtualMachinesStore manager) {
		this.settings = settings;
		this.manager = manager;
	}

	public static VirtualMachineManager create(Settings settings, VirtualMachinesStore manager) {
		VirtualMachineManager vmm = new VirtualMachineManager(settings, manager);
		vmm.updateStates();
		return vmm;
	}

	private void updateStates() {
		List<VirtualMachine> vms = findAll();
		for (var vm: vms) {
			if (vm.getState() != State.STARTED) continue;
			 	boolean isActive = vm.getPid()
					 .flatMap(ProcessHandle::of)
					 .map(ProcessHandle::isAlive)
					 .orElse(false);
			 if (!isActive)
				 cleanup(vm);
		}
	}

	private void cleanup(VirtualMachine vm) {
		vm.setState(State.STOPPED);
		vm.setPid(Optional.empty());
		var sock = vm.getSocket().toFile();
		if (sock.exists()) sock.delete();
		manager.update(asVirtualMachineEntity(vm));
	}

	public VirtualMachine create(String jqExpr) {
		var id = manager.nextId();
		var home = settings.getStage().resolve(id);
		var socket = home.resolve("firecracker.sock");
		try {
			copyFolder(settings.getOriginVM(), home);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		VirtualMachine vm = builder.build(id, State.UNKNOWN, home, socket, null);
		configUtils.update(vm.getVmConfig(), jqExpr);
		manager.add(asVirtualMachineEntity(vm));
		return vm;
	}

	private void copyFolder(Path src, Path dest) throws IOException {
	    Files.walk(src)
	        .forEach(source -> copy(source, dest.resolve(src.relativize(source))));
	}

	private void copy(Path source, Path dest) {
	    try {
	        Files.copy(source, dest, StandardCopyOption.COPY_ATTRIBUTES);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	public VirtualMachine find(String vmId) {
		return asVirtualMachine(manager.findVm(vmId));
	}

	public List<VirtualMachine> findAll() {
		return manager.findAll().stream()
				.map(this::asVirtualMachine)
				.collect(Collectors.toList());
	}

	public List<VirtualMachine> findWithState(State state) {
		return manager.find(state).stream()
				.map(this::asVirtualMachine)
				.collect(Collectors.toList());
	}

	private VirtualMachine asVirtualMachine(VirtualMachineEntity entity) {
		return builder.build(entity.id,
				entity.state,
				Paths.get(entity.homeFolder),
				Paths.get(entity.socket),
				entity.pid);
	}

	private VirtualMachineEntity asVirtualMachineEntity(VirtualMachine vm) {
		var entity = new VirtualMachineEntity();
		entity.id = vm.getId();
		entity.homeFolder = vm.getHome().toString();
		entity.socket = vm.getSocket().toString();
		entity.state = vm.getState();
		entity.pid = vm.getPid().orElse(null);
		return entity;
	}

	public VirtualMachine start(String vmId) {
		VirtualMachine vm = find(vmId);
		Path socket = vm.getSocket();
		if (socket.toFile().exists())
			throw new RuntimeException("Socket file exists: " + socket);
		var pb = new ProcessBuilder("screen",
				"-S",
				vmId,
				"-dm",
				settings.getFirecracker().toString(),
				"--api-sock",
				socket.toString(),
				"--config-file",
				vm.getVmConfig().getLocation().toString());
		pb.directory(vm.getHome().toFile());
		try {
			var proc = pb.start();
			vm.setPid(Optional.of(findPid(vmId)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		vm.setState(State.STARTED);
		manager.update(asVirtualMachineEntity(vm));
		return vm;
	}
	
	private static long findPid(String vmId) {
		var res = new Exec("screen", "-ls").run().stdout
			.filter(l -> l.contains(vmId))
			.collect(Collectors.toList());
		if (res.size() != 1)
			throw new RuntimeException("Cannot find PID of the VM");
		var pid = Long.parseLong(res.get(0).replaceAll("\\s+(\\d+).*", "$1"));
		return pid;
	}

	public void stat() {
		
	}

	public static void main(String[] args) {
		System.out.println(findPid("vm-1"));
	}
}
