package id.fireguard.vmm;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
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

	public VirtualMachineManager(Settings settings, VirtualMachinesStore manager) {
		this.settings = settings;
		this.manager = manager;
	}

	public VirtualMachine create(String ip) {
		validateIfIpAvailable(ip);
		var id = manager.nextId();
		var home = settings.getStage().resolve(id);
		var socket = home.resolve("firecracker.sock");
		try {
			copyFolder(settings.getOriginVM(), home);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		VirtualMachine dbc = builder.build(id, State.UNKNOWN, home, socket);
		configUtils.setIp(dbc.getVmConfig(), ip);
		manager.add(asVirtualMachineEntity(dbc));
		return dbc;
	}

	private void validateIfIpAvailable(String ip) {
		boolean found = findAll().stream().map(VirtualMachine::getVmConfig)
				.map(VmConfig::getIp)
				.anyMatch(Predicate.isEqual(ip));
		if (found) {
			throw new RuntimeException(format("ip %s is already taken", ip));
		}
	}

	private  void copyFolder(Path src, Path dest) throws IOException {
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
				Paths.get(entity.socket));
	}

	private VirtualMachineEntity asVirtualMachineEntity(VirtualMachine vm) {
		var entity = new VirtualMachineEntity();
		entity.id = vm.getId();
		entity.homeFolder = vm.getHome().toString();
		entity.socket = vm.getSocket().toString();
		entity.state = vm.getState();
		return entity;
	}

	public void start(String vmId) {
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
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		vm.setState(State.STARTED);
		manager.update(asVirtualMachineEntity(vm));
	}
	
	public void stat() {
		
	}
}
