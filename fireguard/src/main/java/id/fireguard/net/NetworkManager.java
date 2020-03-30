package id.fireguard.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;

import id.fireguard.net.generators.IpGenerator;
import id.xfunction.function.Unchecked;

public class NetworkManager {

    private NetworkStore networkStore;
    private NetworkTransformer transformer;
    private NetworkManagerConfig config;

    protected NetworkManager(NetworkManagerConfig config, NetworkStore networkStore, NetworkTransformer transformer) {
        this.networkStore = networkStore;
        this.transformer = transformer;
        this.config = config;
    }

	public Network create(String subnet, String netmask) {
        return Unchecked.get(() -> createUnsafe(subnet, netmask));
	}

    private Network createUnsafe(String subnet, String netmask) throws UnknownHostException {
        var id = networkStore.nextId();
        Network net = new Network(id, InetAddress.getByName(subnet), InetAddress.getByName(netmask));
        networkStore.add(transformer.toEntity(net));
        return net;
	}

    public List<Network> findAll() {
        return networkStore.findAll().stream()
                .map(transformer::fromEntity)
                .collect(Collectors.toList());
    }

	public void attach(String vmId, String netId) {
		Optional<Network> netOpt = find(netId);
		if (netOpt.isEmpty())
			throw new RuntimeException("Network with id " + netId + " not found");
		var net = netOpt.get();
		Set<InetAddress> ipPool = net.getInterfaces().stream()
				.map(NetworkInterface::getIp)
				.collect(Collectors.toSet());
		var ip = new IpGenerator().newIp(net.getSubnet(), ipPool);
		ip.orElseThrow(() -> new RuntimeException("Error generating new ip"));
		var mac = config.getLastUsedMacAddr().inc();
		config.setLastUsedMacAddr(mac);
		net.getInterfaces().add(new NetworkInterface(ip.get(), mac));
		networkStore.update(transformer.toEntity(net));
	}

    public Optional<Network> find(String netId) {
    	return networkStore.findNet(netId)
    			.map(transformer::fromEntity);
    }
}
