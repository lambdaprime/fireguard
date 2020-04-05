package id.fireguard.vmm.vmconfig;

import java.util.List;

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
