package id.fireguard.vmm.vmconfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
		JsonObject bootSource = jsonObject
				.get("boot-source").getAsJsonObject();
		String ip = readIp(bootSource.get("boot_args").getAsString());
		var vmConfig =  new VmConfig(vmConfigPath);
		vmConfig.setVcpu(vcpu);
		vmConfig.setMemoryGb(memoryGb);
		vmConfig.setIp(ip);
		return vmConfig;
	}

	public void setIp(VmConfig vmConfig, String ip) {
		Path location = vmConfig.getLocation();
		var json = readFromFile(location);
		json = json.replace(vmConfig.getIp(), ip);
		vmConfig.setIp(ip);
		writeToFile(location, json);
	}

	private String readIp(String line) {
		return line.replaceAll(".*ip=([0-9.]*).*", "$1");
	}


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
		System.out.println(utils.readIp("boot_args\": \"console=ttyS0 reboot=k panic=1 pci=off ip=172.16.0.2"));
	}
}
