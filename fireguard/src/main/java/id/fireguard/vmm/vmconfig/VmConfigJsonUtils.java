/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.vmm.vmconfig;

import static id.xfunction.XUtils.unquote;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.StringJoiner;

import id.jnix.Jq;
import id.xfunction.XAsserts;
import id.xfunction.XProcess;
import id.xfunction.function.Curry;
import id.xfunction.function.Unchecked;

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
                .ifPresent(Unchecked.wrapAccept(Curry.curryAccept1st(this::saveNetworkIface, path)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveNetworkIface(Path path, NetworkIface netIface) throws Exception {
        var isNew = readNetworkIface(path).isEmpty();
        String filter = "";
        if (isNew) {
            var item = format("{\"iface_id\": \"%s\", \"guest_mac\": \"%s\", \"host_dev_name\": \"%s\"}",
                    netIface.getIfaceId(), netIface.getMac(), netIface.getHostDevName());
            filter = format(".\"network-interfaces\" = [%s]", item);
        } else {
            var sj = new StringJoiner("|");
            sj.add(format(".\"network-interfaces\"[0].\"guest_mac\" = \"%s\"",
                    netIface.getMac()));
            sj.add(format(".\"network-interfaces\"[0].\"host_dev_name\" = \"%s\"",
                    netIface.getHostDevName()));
            filter = sj.toString();
        }
        var proc = new XProcess(new Jq()
                .withFile(path)
                .withFilter(filter.toString())
                .withInplaceMode()
                .run());
        var ret = proc.code().get();
        if (ret != 0) {
            throw new RuntimeException(proc.stderrAsString());
        }
    }

    private void saveMachineConfig(Path path, MachineConfig machineConfig) throws Exception {
        var filter = new StringJoiner("|");
        filter.add(".\"machine-config\".\"vcpu_count\" = " + machineConfig.getVcpu());
        filter.add(".\"machine-config\".\"mem_size_mib\" = " + machineConfig.getMemory());
        var proc = new XProcess(new Jq()
                .withFile(path)
                .withFilter(filter.toString())
                .withInplaceMode()
                .run());
        var ret = proc.code().get();
        if (ret != 0) {
            throw new RuntimeException(proc.stderrAsString());
        }
    }

    private Optional<NetworkIface> readNetworkIface(Path location) throws Exception {
    	var proc = new Jq()
            .withFile(location)
            .withFilter(".\"network-interfaces\"[0]| .\"guest_mac\",.\"host_dev_name\",.\"iface_id\"| select (.!=null)")
            .run();
    	var out = new XProcess(proc).stdout().collect(toList());
    	if (out.isEmpty()) return Optional.empty();
    	XAsserts.assertTrue(out.size() == 3, "Unknown network-interfaces");
    	return Optional.of(new NetworkIface(
    			unquote(out.get(2)),
    			unquote(out.get(0)),
    			unquote(out.get(1))));
    }

    private MachineConfig readMachineConfig(Path location) throws Exception {
        var proc = new Jq()
                .withFile(location)
                .withFilter(".\"machine-config\"| .\"vcpu_count\",.\"mem_size_mib\"| select (.!=null)")
                .run();
        var out = new XProcess(proc).stdout().collect(toList());
        XAsserts.assertTrue(out.size() == 2, "Unknown machine-config");
        return new MachineConfig(Integer.parseInt(out.get(0)), Integer.parseInt(out.get(1)));
    }

    public static void main(String[] args) {
        new VmConfigJsonUtils().load(Paths.get("/home/lynx/vms/origs/ubuntu/vm_config.json"));
    }
}
