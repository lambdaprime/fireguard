/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.vmm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import id.fireguard.Settings;
import id.fireguard.net.NetworkInterface;
import id.fireguard.vmm.vmconfig.VmConfigUtils;
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

    public VirtualMachine create(Optional<String> jqExpr) {
        var id = manager.nextId();
        var home = settings.getStage().resolve(id);
        var socket = home.resolve("firecracker.sock");
        try {
            copyFolder(settings.getOriginVM(), home);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        jqExpr.ifPresent(jqexp -> configUtils.update(home, jqexp));
        VirtualMachine vm = builder.build(id, State.STOPPED, home, socket);
        manager.add(asVirtualMachineEntity(vm));
        return vm;
    }

    public VirtualMachine find(String vmId) {
        Supplier<RuntimeException> supply = () -> new RuntimeException("Not found vm with id " + vmId);
        return asVirtualMachine(manager.findVm(vmId).orElseThrow(supply));
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

    public VirtualMachine start(String vmId) {
        VirtualMachine vm = find(vmId);
        Path socket = vm.getSocket();
        if (socket.toFile().exists())
            throw new RuntimeException("Socket file exists: " + socket);
        var pb = new ProcessBuilder("screen",
                "-S",
                vmId,
                "-Dm",
                settings.getFirecracker().toString(),
                "--api-sock",
                socket.toString(),
                "--config-file",
                vm.getVmConfig().getLocation().toString());
        pb.directory(vm.getHome().toFile());
        try {
            var proc = pb.start();
            vm.setPid(Optional.of(proc.pid()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        vm.setState(State.STARTED);
        manager.update(asVirtualMachineEntity(vm));
        return vm;
    }

    public void stop(String vmId) {
        VirtualMachine vm = find(vmId);
        vm.getPid()
            .flatMap(ProcessHandle::of)
            .filter(ProcessHandle::isAlive)
            .ifPresent(ph -> {
                ph.children().forEach(this::killAndWait);
                killAndWait(ph);
            });
        updateStates(vm);
    }

    public VirtualMachine update(String vmId, String jqExpr) {
        VirtualMachine vm = find(vmId);
        configUtils.update(vm.getHome(), jqExpr);
        vm = builder.buildWithNewConfig(vm);
        manager.add(asVirtualMachineEntity(vm));
        return vm;
    }

    private void updateStates() {
        findAll().stream()
            .forEach(this::updateStates);
    }

    private void updateStates(VirtualMachine vm) {
        if (vm.getState() != State.STARTED) {
            cleanup(vm);
            return;
        }
        boolean isActive = vm.getPid()
                .flatMap(ProcessHandle::of)
                .map(ProcessHandle::isAlive)
                .orElse(false);
        if (isActive) return;
        cleanup(vm);
    }

    private void cleanup(VirtualMachine vm) {
        vm.setState(State.STOPPED);
        vm.setPid(Optional.empty());
        var sock = vm.getSocket().toFile();
        if (sock.exists()) sock.delete();
        manager.update(asVirtualMachineEntity(vm));
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

    private VirtualMachine asVirtualMachine(VirtualMachineEntity entity) {
        return builder.build(entity.id,
                entity.state,
                Paths.get(entity.homeFolder),
                Paths.get(entity.socket),
                Optional.ofNullable(entity.pid));
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

    private void killAndWait(ProcessHandle ph) {
        ph.destroy();
        Unchecked.run(() -> ph.onExit().get());
    }

    public void onAttach(NetworkInterface iface) {
        var vmConfig = find(iface.getVmId()).getVmConfig();
        vmConfig.setIface("eth0", iface.getName(), iface.getMac().toString());
        configUtils.save(vmConfig);
    }
}
