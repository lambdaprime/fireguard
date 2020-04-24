/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.net.dhcpd;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DhcpdConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Map<InetAddress, SubnetSection> subnets = new LinkedHashMap<>();

	public DhcpdConfig() {
		
	}

	public DhcpdConfig(SubnetSection subnet) {
		this.subnets = Map.of(subnet.getSubnet(), subnet);
	}

	public void addSubnet(SubnetSection subnet) {
		subnets.put(subnet.getSubnet(), subnet);
	}
	
	public List<SubnetSection> getSubnets() {
		return List.copyOf(subnets.values());
	}
	
	public Optional<SubnetSection> getSubnet(InetAddress subnet) {
		return Optional.ofNullable(subnets.get(subnet));
	}
	
	@Override
	public String toString() {
		return subnets.values().stream()
				.map(SubnetSection::toString)
				.collect(Collectors.joining("\n"));
	}
}
