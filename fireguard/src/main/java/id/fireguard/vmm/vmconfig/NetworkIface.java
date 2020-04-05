package id.fireguard.vmm.vmconfig;

public class NetworkIface {
	
	private String ifaceId;
	private String mac;
	private String hostDevName;
	
	public NetworkIface(String ifaceId, String mac, String hostDevName) {
		this.ifaceId = ifaceId;
		this.mac = mac;
		this.hostDevName = hostDevName;
	}

	public String getIfaceId() {
		return ifaceId;
	}

	public void setIfaceId(String ifaceId) {
		this.ifaceId = ifaceId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getHostDevName() {
		return hostDevName;
	}

	public void setHostDevName(String hostDevName) {
		this.hostDevName = hostDevName;
	}
	
}
