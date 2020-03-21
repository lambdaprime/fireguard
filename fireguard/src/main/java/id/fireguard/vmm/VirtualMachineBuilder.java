package id.fireguard.vmm;

import java.nio.file.Path;
import java.util.Optional;

import id.fireguard.vmm.vmconfig.VmConfig;
import id.fireguard.vmm.vmconfig.VmConfigUtils;

public class VirtualMachineBuilder {

    private VmConfigUtils configUtils = new VmConfigUtils();

    public VirtualMachine build(String id, State state, Path home, Path socket, Optional<Long> pid) {
        VmConfig vmConfig = configUtils.load(home);
        return new VirtualMachine(id,
            state,
            home,
            socket,
            vmConfig,
            pid);
    }

    public VirtualMachine buildWithNewConfig(VirtualMachine vm) {
        VmConfig vmConfig = configUtils.load(vm.getHome());
        return new VirtualMachine(vm.getId(),
            vm.getState(),
            vm.getHome(),
            vm.getSocket(),
            vmConfig,
            vm.getPid());
    }
}
