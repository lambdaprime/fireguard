package id.fireguard.net;

import static java.lang.String.format;

import java.net.InetAddress;

public class Network {

    private String id;
    private InetAddress subnet;
    private InetAddress netmask;

	public Network(String id, InetAddress subnet, InetAddress netmask) {
		this.id = id;
		this.subnet = subnet;
		this.netmask = netmask;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public InetAddress getSubnet() {
		return subnet;
	}

	public void setSubnet(InetAddress subnet) {
		this.subnet = subnet;
	}

	public InetAddress getNetmask() {
		return netmask;
	}

	public void setNetmask(InetAddress netmask) {
		this.netmask = netmask;
	}

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(format("id: %s\n", id));
        sb.append(format("subnet: %s\n", subnet));
        sb.append(format("netmask: %s\n", netmask));
        return sb.toString();
    }
}
