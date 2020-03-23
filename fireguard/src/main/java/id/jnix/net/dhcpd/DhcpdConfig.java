package id.jnix.net.dhcpd;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DhcpdConfig {

	List<Subnet> subnets = List.of();

	public DhcpdConfig(Subnet subnets) {
		this.subnets = Arrays.asList(subnets);
	}

	@Override
	public String toString() {
		return subnets.stream().map(Subnet::toString)
				.collect(Collectors.joining("\n"));
	}
}
