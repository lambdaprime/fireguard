/*
 * Copyright 2020 fireguard project
 * 
 * Website: https://github.com/lambdaprime/fireguard
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.fireguard.net;

import id.xfunction.ObjectStore;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author lambdaprime intid@protonmail.com
 */
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
        if (findAll().stream()
                .map(NetworkEntity::getSubnet)
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
        return set.stream().filter(net -> net.getId().equals(netId)).findFirst();
    }

    public Optional<NetworkInterfaceEntity> findIface(String ifaceId) {
        return set.stream()
                .map(NetworkEntity::getIfaces)
                .flatMap(Set::stream)
                .filter(iface -> iface.getId().equals(ifaceId))
                .findFirst();
    }
}
