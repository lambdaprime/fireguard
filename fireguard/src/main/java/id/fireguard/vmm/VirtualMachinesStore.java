package id.fireguard.vmm;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.List;

import id.xfunction.ObjectsStore;

public class VirtualMachinesStore {

    private ObjectsStore<VirtualMachineEntity> store;

    private VirtualMachinesStore(ObjectsStore<VirtualMachineEntity> store) {
        this.store = store;
    }

    public void add(VirtualMachineEntity entity) {
        store.add(entity);
    }

    public void update(VirtualMachineEntity entity) {
        store.update(entity);
    }

    public void save() {
        store.save();
    }

    public List<VirtualMachineEntity> findAll() {
        return store.findAll();
    }

    public List<VirtualMachineEntity> find(State state) {
        return store.findAll().stream()
                .filter(e -> e.state == state)
                .collect(toList());
    }

    public static VirtualMachinesStore load(Path store) {
        return new VirtualMachinesStore(ObjectsStore.load(store));
    }

    public String nextId() {
        return "vm-" + (store.findAll().size() + 1);
    }

    public VirtualMachineEntity findVm(String vmId) {
        return store.findAll().stream()
                .filter(vm -> vm.id.equals(vmId))
                .findAny().get();
    }
}
