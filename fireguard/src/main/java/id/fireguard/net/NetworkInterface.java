package id.fireguard.net;

import static java.lang.String.format;

import java.net.InetAddress;

/**
 * Represent network interface to which VM is attached on
 * the host system
 */
public class NetworkInterface {

	private String vmId;
	private InetAddress hostIp;
	private InetAddress vmIp;
	private MacAddress macAddress;
    
	public NetworkInterface(String vmId, InetAddress hostIp, InetAddress vmIp, MacAddress macAddress) {
		this.vmId = vmId;
		this.hostIp = hostIp;
		this.vmIp = vmIp;
		this.macAddress = macAddress;
	}
	
    public InetAddress getVmIp() {
		return vmIp;
	}
    
	public MacAddress getMac() {
		return macAddress;
	}

	public InetAddress getHostIp() {
		return hostIp;
	}
	
	public String getVmId() {
		return vmId;
	}
	
    @Override
    public String toString() {
        return format("{ vmId: %s, hostIp: %s, vmIp: %s, macAddress: %s }",
        		vmId, hostIp, vmIp, macAddress);
    }
}
