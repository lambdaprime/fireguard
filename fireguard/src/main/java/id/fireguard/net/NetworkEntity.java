package id.fireguard.net;

import java.io.Serializable;
import java.net.InetAddress;

public class NetworkEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    String id;
    InetAddress subnet;
    InetAddress netmask;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getId() {
		return id;
	}
	
	public InetAddress getSubnet() {
		return subnet;
	}
	
	public InetAddress getNetmask() {
		return netmask;
	}
    
}
