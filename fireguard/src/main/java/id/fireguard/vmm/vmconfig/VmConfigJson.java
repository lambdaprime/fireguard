package id.fireguard.vmm.vmconfig;

import java.util.List;
import java.util.Optional;

import com.google.gson.annotations.SerializedName;

public class VmConfigJson {

	@SerializedName("machine-config")
	private MachineConfig machineConfig;

	@SerializedName("network-interfaces")
	private List<NetworkIface> networkInterfaces;

	public Optional<MachineConfig> getMachineConfig() {
		return Optional.ofNullable(machineConfig);
	}

	public Optional<List<NetworkIface>> getNetworkInterfaces() {
		return Optional.ofNullable(networkInterfaces);
	}
	
}
