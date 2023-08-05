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
package id.fireguard.vmm.vmconfig;

import java.util.List;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class VmConfigJson {

    private MachineConfig machineConfig;
    private List<NetworkIface> networkInterfaces;

    public VmConfigJson(MachineConfig machineConfig, List<NetworkIface> networkInterfaces) {
        this.machineConfig = machineConfig;
        this.networkInterfaces = networkInterfaces;
    }

    public MachineConfig getMachineConfig() {
        return machineConfig;
    }

    public List<NetworkIface> getNetworkInterfaces() {
        return networkInterfaces;
    }

    public void setMachineConfig(MachineConfig machineConfig) {
        this.machineConfig = machineConfig;
    }

    public void setNetworkInterfaces(List<NetworkIface> networkInterfaces) {
        this.networkInterfaces = networkInterfaces;
    }
}
