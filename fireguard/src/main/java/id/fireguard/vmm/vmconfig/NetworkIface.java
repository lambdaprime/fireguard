package id.fireguard.vmm.vmconfig;

import com.google.gson.annotations.SerializedName;

public class NetworkIface {
	
	@SerializedName("iface_id")
	String ifaceId;

	@SerializedName("guest_mac")
	String mac;

	@SerializedName("host_dev_name")
	String hostDevName;

}
