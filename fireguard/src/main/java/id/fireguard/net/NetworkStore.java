package id.fireguard.net;

import java.nio.file.Path;
import java.util.List;

import id.xfunction.ObjectsStore;

public class NetworkStore {

    private ObjectsStore<NetworkEntity> store;

    private NetworkStore(ObjectsStore<NetworkEntity> store) {
        this.store = store;
    }

    public void add(NetworkEntity entity) {
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

    public String nextId() {
        return "net-" + (store.findAll().size() + 1);
    }

    public NetworkEntity findNet(String netId) {
        return store.findAll().stream()
                .filter(vm -> vm.id.equals(netId))
                .findAny().get();
    }
}
