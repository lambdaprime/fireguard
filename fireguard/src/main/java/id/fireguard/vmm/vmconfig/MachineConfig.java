package id.fireguard.vmm.vmconfig;

import com.google.gson.annotations.SerializedName;

public class MachineConfig {

	@SerializedName("vcpu_count")
	int vcpu;

	@SerializedName("mem_size_mib")
	int memory;

}
