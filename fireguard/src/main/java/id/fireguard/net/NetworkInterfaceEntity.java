package id.fireguard.net;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;

public class NetworkInterfaceEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	InetAddress ip;
    MacAddress macAddress;
    
    public InetAddress getIp() {
		return ip;
	}
    
	public MacAddress getMac() {
		return macAddress;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        NetworkInterfaceEntity r = (NetworkInterfaceEntity) obj;
        return Objects.equals(ip, r.ip)
        		&& Objects.equals(macAddress, r.macAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, macAddress);
    }
}
