package id.fireguard.vmm;

import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import id.fireguard.ObjectsStore;

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

	public static void main(String[] args) {
		Path store = Paths.get("/tmp/store");
		VirtualMachinesStore pm = load(store);
		VirtualMachineEntity entity1 = new VirtualMachineEntity();
		entity1.homeFolder = "/tmp/lol";
		entity1.socket = "/tmp/sock";
		pm.add(entity1);

		VirtualMachineEntity entity2 = new VirtualMachineEntity();
		entity2.homeFolder = "/tmp/lol2";
		entity2.socket = "/tmp/sock1";
		pm.add(entity2);

		pm.save();

		pm = load(store);
		pm.findAll().forEach(out::println);
	}

	public String nextId() {
		return "dbc-" + (store.findAll().size() + 1);
	}

	public VirtualMachineEntity findVm(String vmId) {
		return store.findAll().stream()
				.filter(vm -> vm.id.equals(vmId))
				.findAny().get();
	}
}
