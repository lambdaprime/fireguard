package id.fireguard.net;

import static java.lang.String.format;

import java.net.InetAddress;

public class NetworkInterface {

	private InetAddress ip;
	private MacAddress macAddress;
    
	public NetworkInterface(InetAddress ip, MacAddress macAddress) {
		this.ip = ip;
		this.macAddress = macAddress;
	}
	
    public InetAddress getIp() {
		return ip;
	}
    
	public MacAddress getMac() {
		return macAddress;
	}

    @Override
    public String toString() {
        return format("{ ip: %s, macAddress: %s }", ip, macAddress);
    }
}
