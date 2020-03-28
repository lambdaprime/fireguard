package id.fireguard.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import id.xfunction.function.Unchecked;

public class NetworkManager {

    private NetworkStore manager;
    private NetworkTransformer transformer;

    private NetworkManager(NetworkStore manager, NetworkTransformer transformer) {
        this.manager = manager;
        this.transformer = transformer;
    }

	public Network create(String subnet, String netmask) {
        return Unchecked.get(() -> createUnsafe(subnet, netmask));
	}

    private Network createUnsafe(String subnet, String netmask) throws UnknownHostException {
        var id = manager.nextId();
        Network net = new Network(id, InetAddress.getByName(subnet), InetAddress.getByName(netmask));
        manager.add(transformer.toEntity(net));
        return net;
	}

    public static NetworkManager create(NetworkStore manager) {
    	NetworkManager nm = new NetworkManager(manager, new NetworkTransformer());
        return nm;
    }

}
