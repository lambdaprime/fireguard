package id.fireguard.vmm.vmconfig;

import java.nio.file.Path;

import id.jnix.Jq;
import id.xfunction.XProcess;

public class VmConfigUtils {

    private static final String VM_CONFIG_JSON = "vm_config.json";
    private final VmConfigJsonUtils vmJsonUtils;

    public VmConfigUtils() {
    	this(new VmConfigJsonUtils());
    }

    public VmConfigUtils(VmConfigJsonUtils vmJsonUtils) {
		this.vmJsonUtils = vmJsonUtils;
	}

    public VmConfig load(Path vmHome) {
        Path vmConfigPath = vmHome.resolve(VM_CONFIG_JSON);
        var vmConfig =  new VmConfig(vmConfigPath, vmJsonUtils.load(vmConfigPath));
        return vmConfig;
    }

    public void save(VmConfig vmConfig) {
    	vmJsonUtils.save(vmConfig.getLocation(), vmConfig.getVmConfigJson());
    }
    
    public void update(Path vmHome, String jqExpr) {
        Path vmConfigPath = vmHome.resolve(VM_CONFIG_JSON);
        try {
        	var proc = new XProcess(new Jq().withFile(vmConfigPath)
            		.withFilter(jqExpr)
            		.withInplaceMode()
            		.run());
        	var ret = proc.code().get();
        	if (ret != 0) {
        		throw new RuntimeException(proc.stderrAsString());
        	}
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
