package id.jnix.net.dhcpd;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SubnetSection {

	private InetAddress subnet;
	private InetAddress netmask;
	private List<Host> hosts = new ArrayList<>();
	
	public SubnetSection(InetAddress subnet, InetAddress netmask, Host...hosts) {
		this.subnet = subnet;
		this.netmask = netmask;
		this.hosts.addAll(Arrays.asList(hosts));
	}

	public InetAddress getSubnet() {
		return subnet;
	}
	
	public List<Host> getHosts() {
		return hosts;
	}
	
	public void addHost(Host host) {
		hosts.add(host);
	}
	
	public InetAddress getNetmask() {
		return netmask;
	}
	
	@Override
	public String toString() {
		return String.format("subnet %s netmask %s { %s }", subnet.getHostAddress(),
				netmask.getHostAddress(),
				hosts.stream().map(Host::toString)
				.collect(Collectors.joining("\n")));
	}
}
