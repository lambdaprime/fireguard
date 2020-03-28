package id.fireguard.net;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import id.xfunction.ObjectsStore;

public class NetworkStore {

    private ObjectsStore<NetworkEntity> store;

    private NetworkStore(ObjectsStore<NetworkEntity> store) {
        this.store = store;
    }

    public void add(NetworkEntity entity) {
    	if (findAll().stream().map(NetworkEntity::getSubnet)
    			.anyMatch(Predicate.isEqual(entity.getSubnet())))
    		throw new RuntimeException("Net with such subnet already exist");
        store.add(entity);
    }

    public void update(NetworkEntity entity) {
        store.update(entity);
    }

    public void save() {
        store.save();
    }

    public List<NetworkEntity> findAll() {
        return store.findAll();
    }

    public static NetworkStore load(Path store) {
        return new NetworkStore(ObjectsStore.load(store));
    }

    public static NetworkStore create(ObjectsStore<NetworkEntity> store) {
        return new NetworkStore(store);
    }

    public String nextId() {
        return "net-" + (store.findAll().size() + 1);
    }

    public NetworkEntity findNet(String netId) {
        return store.findAll().stream()
                .filter(net -> net.getId().equals(netId))
                .findAny().get();
    }
}
