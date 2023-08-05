/*
 * Copyright 2020 fireguard project
 * 
 * Website: https://github.com/lambdaprime/fireguard
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.fireguard.vmm;

import id.fireguard.vmm.vmconfig.VmConfig;
import id.fireguard.vmm.vmconfig.VmConfigUtils;
import java.nio.file.Path;
import java.util.Optional;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class VirtualMachineBuilder {

    private VmConfigUtils configUtils = new VmConfigUtils();

    public VirtualMachine build(
            String id, State state, Path home, Path socket, Optional<Long> pid) {
        var vm = build(id, state, home, socket);
        pid.ifPresent(p -> vm.withPid(p));
        return vm;
    }

    public VirtualMachine build(String id, State state, Path home, Path socket) {
        VmConfig vmConfig = configUtils.load(home);
        return new VirtualMachine(id, state, home, socket, vmConfig);
    }

    public VirtualMachine buildWithNewConfig(VirtualMachine vm) {
        VmConfig vmConfig = configUtils.load(vm.getHome());
        var newvm =
                new VirtualMachine(
                        vm.getId(), vm.getState(), vm.getHome(), vm.getSocket(), vmConfig);
        vm.getPid().ifPresent(p -> newvm.withPid(p));
        return newvm;
    }
}
