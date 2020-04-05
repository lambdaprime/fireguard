package id.fireguard.vmm.vmconfig;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import id.jnix.Jq;
import id.xfunction.XAsserts;
import id.xfunction.XProcess;

public class VmConfigJsonUtils {

    public VmConfigJson load(Path location) {
        try {
        	var machineConfig = machineConfig(location);
        	var networkInterfaces = new ArrayList<NetworkIface>();
        	networkIface(location).ifPresent(networkInterfaces::add);
            return new VmConfigJson(machineConfig, networkInterfaces);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private Optional<NetworkIface> networkIface(Path location) throws Exception {
    	var proc = new Jq()
	    		.withFile(location)
	    		.withFilter(".\"network-interfaces\"[0]| .\"guest_mac\",.\"host_dev_name\",.\"iface_id\"| select (.!=null)")
	    		.run();
    	var out = new XProcess(proc).stdout().collect(toList());
    	if (out.isEmpty()) return Optional.empty();
    	XAsserts.assertTrue(out.size() == 3, "Unknown network-interfaces");
    	return Optional.of(new NetworkIface(out.get(2), out.get(0), out.get(1)));
	}

	private MachineConfig machineConfig(Path location) throws Exception {
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
