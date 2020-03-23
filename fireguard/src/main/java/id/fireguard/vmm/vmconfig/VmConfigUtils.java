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

import id.xfunction.XExec;
import id.xfunction.XProcess;

public class VmConfigUtils {

    private static final String VM_CONFIG_JSON = "vm_config.json";

    public VmConfig load(Path vmHome) {
        Path vmConfigPath = vmHome.resolve(VM_CONFIG_JSON);
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
        return vmConfig;
    }

    public void update(Path vmHome, String jqExpr) {
        Path vmConfigPath = vmHome.resolve(VM_CONFIG_JSON);
        try {
            var json = readFromFile(vmConfigPath);
            XProcess result = new XExec("jq", jqExpr)
                    .withInput(Files.readAllLines(vmConfigPath).stream())
                    .run();
            if (result.code().get() != 0) {
                var msg = asString(result.stderr());
                throw new RuntimeException(msg);
            }
            json = asString(result.stdout());
            writeToFile(vmConfigPath, json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private String asString(Stream<String> stream) {
        return stream.collect(joining("\n"));
    }
}
