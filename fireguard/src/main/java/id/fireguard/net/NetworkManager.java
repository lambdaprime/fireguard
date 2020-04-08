package id.fireguard.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Set;
import java.util.function.Consumer;

import id.fireguard.net.generators.IpGenerator;
import id.xfunction.XObservable;
import id.xfunction.function.Unchecked;

public class NetworkManager {

    private NetworkStore networkStore;
    private NetworkTransformer transformer;
    private NetworkManagerConfig config;
    private XObservable<NetworkInterface> onAttach = new XObservable<>();

    protected NetworkManager(NetworkManagerConfig config, NetworkStore networkStore, NetworkTransformer transformer) {
        this.networkStore = networkStore;
        this.transformer = transformer;
        this.config = config;
    }

	public Network create(String subnet, String netmask) {
        return Unchecked.get(() -> createUnsafe(subnet, netmask));
	}

    private Network createUnsafe(String subnet, String netmask) throws UnknownHostException {
        var id = nextNetId();
        Network net = new Network(id, InetAddress.getByName(subnet), InetAddress.getByName(netmask));
        networkStore.add(transformer.toEntity(net));
        return net;
	}

    private String nextNetId() {
    	var id = config.getLastId() + 1;
    	config.setLastId(id);
    	return "net-" + id;
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
				.flatMap(ni -> Stream.of(ni.getHostIp(), ni.getVmIp()))
				.collect(Collectors.toSet());
		var name = netId + vmId;
		var ipGen = new IpGenerator(ipPool);
		var hostIp = nextIp(ipGen, net.getSubnet());
		var vmIp = nextIp(ipGen, net.getSubnet());
		var mac = config.getLastUsedMacAddr().inc();
		NetworkInterface iface = new NetworkInterface(name, vmId, hostIp, vmIp, mac);
		onAttach.updateAll(iface);
		config.setLastUsedMacAddr(mac);
		net.getInterfaces().add(iface);
		networkStore.update(transformer.toEntity(net));
	}

	public void onAttach(Consumer<NetworkInterface> listener) {
		onAttach.addListener(listener);
	}
	
    private InetAddress nextIp(IpGenerator ipGen, InetAddress subnet) {
    	var ip = ipGen.newIp(subnet);
		ip.orElseThrow(() -> new RuntimeException("Error generating new ip"));
		return ip.get();
	}

	public Optional<Network> find(String netId) {
    	return networkStore.findNet(netId)
    			.map(transformer::fromEntity);
    }

}
