package id.fireguard.net;

public class NetworkTransformer {

	public NetworkEntity toEntity(Network n) {
		var ne = new NetworkEntity();
		ne.id = n.getId();
		ne.subnet = n.getSubnet();
		ne.netmask = n.getNetmask();
		return ne;
	}

	public Network fromEntity(NetworkEntity ne) {
		var net = new Network(ne.id, ne.subnet, ne.netmask);
		return net;
	}

}
