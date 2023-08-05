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
package id.fireguard.vmm;

import static java.util.stream.Collectors.toList;

import id.xfunction.ObjectStore;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class VirtualMachinesStore {

    private ObjectStore<HashSet<VirtualMachineEntity>> store;
    private HashSet<VirtualMachineEntity> set;

    public VirtualMachinesStore(Path file) {
        this.store = new ObjectStore<>(file);
        this.set = store.load().orElse(new HashSet<>());
    }

    public void add(VirtualMachineEntity entity) {
        set.add(entity);
        save();
    }

    public void update(VirtualMachineEntity entity) {
        set.remove(entity);
        set.add(entity);
        save();
    }

    public void save() {
        store.save(set);
    }

    public Set<VirtualMachineEntity> findAll() {
        return set;
    }

    public List<VirtualMachineEntity> find(State state) {
        return set.stream().filter(e -> e.state == state).collect(toList());
    }

    public String nextId() {
        return "vm-" + (set.size() + 1);
    }

    public Optional<VirtualMachineEntity> findVm(String vmId) {
        return set.stream().filter(vm -> vm.id.equals(vmId)).findAny();
    }
}
