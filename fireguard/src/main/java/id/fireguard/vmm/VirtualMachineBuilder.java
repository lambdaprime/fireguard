/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.vmm;

import java.nio.file.Path;
import java.util.Optional;

import id.fireguard.vmm.vmconfig.VmConfig;
import id.fireguard.vmm.vmconfig.VmConfigUtils;

public class VirtualMachineBuilder {

    private VmConfigUtils configUtils = new VmConfigUtils();

    public VirtualMachine build(String id, State state, Path home, Path socket, Optional<Long> pid) {
        var vm = build(id, state, home, socket);
        pid.ifPresent(p -> vm.withPid(p));
        return vm;
    }

    public VirtualMachine build(String id, State state, Path home, Path socket) {
        VmConfig vmConfig = configUtils.load(home);
        return new VirtualMachine(id,
                state,
                home,
                socket,
                vmConfig);
    }

    public VirtualMachine buildWithNewConfig(VirtualMachine vm) {
        VmConfig vmConfig = configUtils.load(vm.getHome());
        var newvm = new VirtualMachine(vm.getId(),
                vm.getState(),
                vm.getHome(),
                vm.getSocket(),
                vmConfig);
        vm.getPid().ifPresent(p -> newvm.withPid(p));
        return newvm;
    }
}
