package id.fireguard.vmm.vmconfig;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import id.xfunction.Exec;
import id.xfunction.Exec.Result;

public class VmConfigUtils {

	public VmConfig load(Path vmHome) {
		Path vmConfigPath = vmHome.resolve("vm_config.json");
		var json = readFromFile(vmConfigPath);
		JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
		JsonObject machineConfig = jsonObject.get("machine-config").getAsJsonObject();
		int vcpu = machineConfig
				.get("vcpu_count").getAsInt();
		int memoryGb = machineConfig
				.get("mem_size_mib").getAsInt() / 1000;
		var vmConfig =  new VmConfig(vmConfigPath);
		vmConfig.setVcpu(vcpu);
		vmConfig.setMemoryGb(memoryGb);
//		JsonObject networkIface = ofNullable(jsonObject)
//				.map(o -> o.get("network-interfaces"))
//				.map(o -> o.getAsJsonArray())
//				.filter(a -> a.size() > 0)
//				.map(a -> a.get(0))
//				.map(o -> o.getAsJsonObject()).orElse(null);
//		if (networkIface != null) {
//			ofNullable(networkIface.get("host_dev_name"))
//				.map(o -> o.getAsString())
//				.ifPresent(o -> vmConfig.setHostIface(o));
//			ofNullable(networkIface.get("guest_mac"))
//				.map(o -> o.getAsString())
//				.ifPresent(o -> vmConfig.setMac(o));
//		}
		return vmConfig;
	}

//	public void setHostIface(VmConfig vmConfig, String hostIface) {
//		Path location = vmConfig.getLocation();
//		var json = readFromFile(location);
//		if (vmConfig.getHostIface().isPresent())
//			json = json.replace(vmConfig.getHostIface().get(), hostIface);
//		vmConfig.setHostIface(hostIface);
//		writeToFile(location, json);
//	}
//
//	public void setMac(VmConfig vmConfig, String mac) {
//		Path location = vmConfig.getLocation();
//		var json = readFromFile(location);
//		if (vmConfig.getMac().isPresent())
//			json = json.replace(vmConfig.getMac().get(), mac);
//		vmConfig.setMac(mac);
//		writeToFile(location, json);
//	}

	private void writeToFile(Path location, String data) {
		try {
			Files.writeString(location, data, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String readFromFile(Path location) {
		try {
			return Files.readAllLines(location).stream().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		VmConfigUtils utils = new VmConfigUtils();
	}

	public void update(VmConfig vmConfig, String jqExpr) {
		try {
			var location = vmConfig.getLocation();
			var json = readFromFile(location);
			Result result = new Exec("jq", jqExpr)
				.withInput(Files.readAllLines(vmConfig.getLocation()).stream())
				.run();
			if (result.code.get() != 0) {
				var msg = asString(result.stderr);
				throw new RuntimeException(msg);
			}
			json = asString(result.stdout);
			writeToFile(location, json);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String asString(Stream<String> stream) {
		return stream.collect(joining("\n"));
	}
}
