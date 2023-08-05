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

import static id.xfunction.XUtils.unquote;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import id.jnix.Jq;
import id.xfunction.Preconditions;
import id.xfunction.function.Curry;
import id.xfunction.function.Unchecked;
import id.xfunction.lang.XProcess;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class VmConfigJsonUtils {

    public VmConfigJson load(Path location) {
        try {
            var machineConfig = readMachineConfig(location);
            var networkInterfaces = new ArrayList<NetworkIface>();
            readNetworkIface(location).ifPresent(networkInterfaces::add);
            return new VmConfigJson(machineConfig, networkInterfaces);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Path path, VmConfigJson vmConfigJson) {
        try {
            saveMachineConfig(path, vmConfigJson.getMachineConfig());
            vmConfigJson.getNetworkInterfaces().stream()
                    .findFirst()
                    .ifPresent(
                            Unchecked.wrapAccept(
                                    Curry.curryAccept1st(this::saveNetworkIface, path)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveNetworkIface(Path path, NetworkIface netIface) throws Exception {
        var isNew = readNetworkIface(path).isEmpty();
        String filter = "";
        if (isNew) {
            var item =
                    format(
                            "{\"iface_id\": \"%s\", \"guest_mac\": \"%s\", \"host_dev_name\":"
                                    + " \"%s\"}",
                            netIface.getIfaceId(), netIface.getMac(), netIface.getHostDevName());
            filter = format(".\"network-interfaces\" = [%s]", item);
        } else {
            var sj = new StringJoiner("|");
            sj.add(format(".\"network-interfaces\"[0].\"guest_mac\" = \"%s\"", netIface.getMac()));
            sj.add(
                    format(
                            ".\"network-interfaces\"[0].\"host_dev_name\" = \"%s\"",
                            netIface.getHostDevName()));
            filter = sj.toString();
        }
        var proc =
                new XProcess(
                        new Jq()
                                .withFile(path)
                                .withFilter(filter.toString())
                                .withInplaceMode()
                                .run());
        var ret = proc.code().get();
        if (ret != 0) {
            throw new RuntimeException(proc.stderr());
        }
    }

    private void saveMachineConfig(Path path, MachineConfig machineConfig) throws Exception {
        var filter = new StringJoiner("|");
        filter.add(".\"machine-config\".\"vcpu_count\" = " + machineConfig.getVcpu());
        filter.add(".\"machine-config\".\"mem_size_mib\" = " + machineConfig.getMemory());
        var proc =
                new XProcess(
                        new Jq()
                                .withFile(path)
                                .withFilter(filter.toString())
                                .withInplaceMode()
                                .run());
        var ret = proc.code().get();
        if (ret != 0) {
            throw new RuntimeException(proc.stderr());
        }
    }

    private Optional<NetworkIface> readNetworkIface(Path location) throws Exception {
        var proc =
                new Jq()
                        .withFile(location)
                        .withFilter(
                                ".\"network-interfaces\"[0]|"
                                        + " .\"guest_mac\",.\"host_dev_name\",.\"iface_id\"| select"
                                        + " (.!=null)")
                        .run();
        var out = new XProcess(proc).stdoutAsStream().collect(toList());
        if (out.isEmpty()) return Optional.empty();
        Preconditions.isTrue(out.size() == 3, "Unknown network-interfaces");
        return Optional.of(
                new NetworkIface(unquote(out.get(2)), unquote(out.get(0)), unquote(out.get(1))));
    }

    private MachineConfig readMachineConfig(Path location) throws Exception {
        var proc =
                new Jq()
                        .withFile(location)
                        .withFilter(
                                ".\"machine-config\"| .\"vcpu_count\",.\"mem_size_mib\"| select"
                                        + " (.!=null)")
                        .run();
        var out = new XProcess(proc).stdoutAsStream().collect(toList());
        Preconditions.isTrue(out.size() == 2, "Unknown machine-config");
        return new MachineConfig(Integer.parseInt(out.get(0)), Integer.parseInt(out.get(1)));
    }

    public static void main(String[] args) {
        new VmConfigJsonUtils().load(Paths.get("/home/lynx/vms/origs/ubuntu/vm_config.json"));
    }
}
