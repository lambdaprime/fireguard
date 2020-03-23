package id.fireguard.vmm.vmconfig;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class VmConfigJson {

	@SerializedName("machine-config")
	MachineConfig machineConfig;

	@SerializedName("network-interfaces")
	List<NetworkIface> networkInterfaces;

}
