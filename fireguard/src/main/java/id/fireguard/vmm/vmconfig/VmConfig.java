/*
 * Copyright 2023 fireguard project
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

import static java.lang.String.format;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class VmConfig {

    private Path location;
    private VmConfigJson json;

    VmConfig(Path location, VmConfigJson json) {
        this.location = location;
        this.json = json;
    }

    public int getMemoryGb() {
        return json.getMachineConfig().getMemory();
    }

    void setMemoryGb(int memoryGb) {
        json.getMachineConfig().setMemory(memoryGb);
    }

    public int getVcpu() {
        return json.getMachineConfig().getVcpu();
    }

    void setVcpu(int vcpu) {
        json.getMachineConfig().setVcpu(vcpu);
    }

    public Optional<String> getHostIface() {
        return json.getNetworkInterfaces().stream().findFirst().map(i -> i.getHostDevName());
    }

    public Optional<String> getMac() {
        return json.getNetworkInterfaces().stream().findFirst().map(i -> i.getMac());
    }

    public Path getLocation() {
        return location;
    }

    public VmConfigJson getVmConfigJson() {
        return json;
    }

    public void setIface(String vmIface, String hostIface, String mac) {
        List<NetworkIface> ifaces = json.getNetworkInterfaces();
        ifaces.stream()
                .findFirst()
                .ifPresentOrElse(
                        i -> {
                            i.setMac(mac);
                            i.setHostDevName(hostIface);
                        },
                        () -> ifaces.add(new NetworkIface(vmIface, mac, hostIface)));
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(format("path: %s\n", location.toString()));
        sb.append(format("memoryGb: %s\n", getMemoryGb()));
        sb.append(format("vcpu: %s\n", getVcpu()));
        sb.append(format("hostIface: %s\n", getHostIface()));
        sb.append(format("mac: %s\n", getMac()));
        return sb.toString();
    }
}
