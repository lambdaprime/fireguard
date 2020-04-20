/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.net;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import id.xfunction.ObjectStore;

public class NetworkStore {

    private ObjectStore<HashSet<NetworkEntity>> store;
    private HashSet<NetworkEntity> set;

    public NetworkStore(Path file) {
        this(new ObjectStore<>(file));
    }

    public NetworkStore(ObjectStore<HashSet<NetworkEntity>> store) {
        this.store = store;
        this.set = store.load().orElse(new HashSet<>());
    }

    public void add(NetworkEntity entity) {
        if (findAll().stream().map(NetworkEntity::getSubnet)
                .anyMatch(Predicate.isEqual(entity.getSubnet())))
            throw new RuntimeException("Net with such subnet already exist");
        set.add(entity);
        save();
    }

    public void update(NetworkEntity entity) {
        set.remove(entity);
        set.add(entity);
        save();
    }

    public void save() {
        store.save(set);
    }

    public Set<NetworkEntity> findAll() {
        return set;
    }

    public Optional<NetworkEntity> findNet(String netId) {
        return set.stream()
                .filter(net -> net.getId().equals(netId))
                .findAny();
    }
}
