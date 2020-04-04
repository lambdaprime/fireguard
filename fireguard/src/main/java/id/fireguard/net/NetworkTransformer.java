package id.fireguard.net;

import java.util.stream.Collectors;

public class NetworkTransformer {

	public NetworkInterfaceEntity toEntity(NetworkInterface n) {
		var ne = new NetworkInterfaceEntity();
		ne.vmId = n.getVmId();
		ne.hostIp = n.getHostIp();
		ne.vmIp = n.getVmIp();
		ne.macAddress = n.getMac();
		return ne;
	}

	public NetworkEntity toEntity(Network n) {
		var ne = new NetworkEntity(n.getId(), n.getSubnet(), n.getNetmask());
		ne.ifaces = n.getInterfaces().stream()
				.map(this::toEntity)
				.collect(Collectors.toSet());
		return ne;
	}

	public NetworkInterface fromEntity(NetworkInterfaceEntity ne) {
		var iface = new NetworkInterface(ne.getVmId(), ne.getHostIp(),
				ne.getVmIp(), ne.getMac());
		return iface;
	}

	public Network fromEntity(NetworkEntity ne) {
		var net = new Network(ne.id, ne.subnet, ne.netmask);
		if (ne.ifaces != null)
			net.withInterfaces(ne.ifaces.stream()
					.map(this::fromEntity)
					.collect(Collectors.toSet()));
		return net;
	}

}
