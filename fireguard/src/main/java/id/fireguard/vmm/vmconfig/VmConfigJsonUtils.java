package id.fireguard.vmm.vmconfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;

public class VmConfigJsonUtils {

    public VmConfigJson load(Path location) {
        try {
        	Gson gson = new Gson();
        	var vmConfig = gson.fromJson(Files.newBufferedReader(location), VmConfigJson.class);
        	//System.out.println(gson.toJson(vmConfig));
            return vmConfig;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) {
		new VmConfigJsonUtils().load(Paths.get("/home/ubuntu/vms/orc/stage/vm-2/vm_config.json"));
	}
}
