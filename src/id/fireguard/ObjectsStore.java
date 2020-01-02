package id.fireguard;

import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObjectsStore<T> {

	private Path store;
	private Set<T> entities;

	private ObjectsStore(Path store, Set<T> entities) {
		this.store = store;
		this.entities = entities;
	}

	public void add(T entity) {
		entities.add(entity);
		save();
	}

	public void update(T entity) {
		entities.remove(entity);
		entities.add(entity);
		save();
	}

	public void save() {
		try (FileOutputStream f = new FileOutputStream(store.toFile());
				ObjectOutputStream o = new ObjectOutputStream(f);) {
			o.writeObject(entities);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<T> findAll() {
		return entities.stream()
				.collect(toList());
	}

	public static <T> ObjectsStore<T> load(Path store) {
		File file = store.toFile();
		if (!file.exists())
			return new ObjectsStore<T>(store, new HashSet<>());
		try (FileInputStream fi = new FileInputStream(store.toFile());
				ObjectInputStream oi = new ObjectInputStream(fi);) {
			Set<T> entities = (Set<T>) oi.readObject();
			return new ObjectsStore(store, entities);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		Path store = Paths.get("/tmp/store");
		ObjectsStore<String> pm = load(store);
		pm.add("enity1");
		pm.add("enity2");

		pm.save();

		pm = load(store);
		pm.findAll().forEach(out::println);
	}

}