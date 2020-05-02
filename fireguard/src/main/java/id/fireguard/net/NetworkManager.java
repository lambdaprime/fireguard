/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.net;

import static java.lang.String.format;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import id.fireguard.net.generators.IpGenerator;
import id.xfunction.XObservable;
import id.xfunction.function.Unchecked;

public class NetworkManager {

    private NetworkStore networkStore;
    private NetworkTransformer transformer;
    private NetworkManagerConfig config;
    private NetworkInstaller intaller;
    
    private XObservable<NetworkInterface> onAfterAttach = new XObservable<>();

    protected NetworkManager(NetworkManagerConfig config,
    		NetworkStore networkStore,
    		NetworkTransformer transformer,
    		NetworkInstaller intaller) {
        this.networkStore = networkStore;
        this.transformer = transformer;
        this.config = config;
        this.intaller = intaller;
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
        var id = config.getLastNetId() + 1;
        config.setLastNetId(id);
        return "net-" + id;
    }

    private String nextIfaceId() {
        var id = config.getLastIfaceId() + 1;
        config.setLastIfaceId(id);
        return "tap" + id;
    }

    public void attach(String vmId, String netId) {
        Optional<Network> netOpt = find(netId);
        if (netOpt.isEmpty())
            throw new RuntimeException("Network with id " + netId + " not found");
        var net = netOpt.get();
        Set<InetAddress> ipPool = net.getInterfaces().stream()
                .flatMap(ni -> Stream.of(ni.getHostIp(), ni.getVmIp()))
                .collect(Collectors.toSet());
        var name = nextIfaceId();
        var ipGen = new IpGenerator(ipPool);
        var hostIp = nextIp(ipGen, net.getSubnet());
        var vmIp = nextIp(ipGen, net.getSubnet());
        var mac = config.getLastUsedMacAddr().inc();
        NetworkInterface iface = new NetworkInterface(name, vmId, hostIp, vmIp, mac);
        config.setLastUsedMacAddr(mac);
        net.getInterfaces().add(iface);
        networkStore.update(transformer.toEntity(net));
        onAfterAttach.updateAll(iface);
    }

    public void addOnAfterAttachListener(Consumer<NetworkInterface> listener) {
        onAfterAttach.addListener(listener);
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
	
    public List<Network> findAll() {
        return networkStore.findAll().stream()
                .map(transformer::fromEntity)
                .collect(Collectors.toList());
    }
	
    public NetworkInterface findIface(String ifaceId) {
		Supplier<RuntimeException> supply = () ->
			new RuntimeException("Not found net iface with id " + ifaceId);
    	return networkStore.findIface(ifaceId)
    			.map(transformer::fromEntity)
    			.orElseThrow(supply);
    }

    public void onBeforeVmStart(String ifaceId) {
        var iface = findIface(ifaceId);
        var network = findAll().stream()
            .filter(n -> n.getInterfaces().stream().filter(ni ->
                ifaceId.equals(ni.getName())).findAny().isPresent())
            .findAny()
            .orElseThrow(() -> new RuntimeException(format("Could not find network for network iface with id %s",
                    ifaceId)));
        intaller.setup(network, iface);
    }
}
