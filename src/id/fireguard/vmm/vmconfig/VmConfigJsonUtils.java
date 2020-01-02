package id.fireguard.vmm.vmconfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VmConfigJsonUtils {

	public VmConfigJson load(Path location) {
		try {
			var json = Files.readAllLines(location).stream().collect(Collectors.joining("\n"));
			JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
			JsonObject machineConfig = jsonObject.get("machine-config").getAsJsonObject();
			int vcpu = machineConfig
					.get("vcpu_count").getAsInt();
			int memoryGb = machineConfig
					.get("mem_size_mib").getAsInt();
			var vmConfig =  new VmConfigJson();
			vmConfig.setVcpu(vcpu);
			vmConfig.setMemoryGb(memoryGb);
			return vmConfig;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

//	public VmConfigJson migrate(Path srcLocation, Path dstLocation) {
//		try {
//			var json = Files.readAllLines(srcLocation).stream().collect(Collectors.joining("\n"));
//			JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
//			JsonObject machineCOnfig = jsonObject.get("logger").getAsJsonObject().get("log_fifo");
//			int vcpu = machineCOnfig
//					.get("vcpu_count").getAsInt();
//			int memoryGb = machineCOnfig
//					.get("mem_size_mib").getAsInt();
//			var vmConfig =  new VmConfigJson();
//			vmConfig.setVcpu(vcpu);
//			vmConfig.setMemoryGb(memoryGb);
//			return vmConfig;
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}
}
