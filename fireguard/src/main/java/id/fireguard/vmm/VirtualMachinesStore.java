package id.fireguard.vmm;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import id.xfunction.ObjectStore;

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
        return set.stream()
                .filter(e -> e.state == state)
                .collect(toList());
    }

    public String nextId() {
        return "vm-" + (set.size() + 1);
    }

    public VirtualMachineEntity findVm(String vmId) {
        return set.stream()
                .filter(vm -> vm.id.equals(vmId))
                .findAny().get();
    }
}
