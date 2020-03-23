package id.fireguard.vmm.vmconfig;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import id.xfunction.XExec;
import id.xfunction.XProcess;

public class VmConfigUtils {

    private static final String VM_CONFIG_JSON = "vm_config.json";
    private final VmConfigJsonUtils vmJsonUtils;

    public VmConfigUtils() {
    	this(new VmConfigJsonUtils());
    }

    public VmConfigUtils(VmConfigJsonUtils vmJsonUtils) {
		this.vmJsonUtils = vmJsonUtils;
	}

    public VmConfig load(Path vmHome) {
        Path vmConfigPath = vmHome.resolve(VM_CONFIG_JSON);
        var vmConfig =  new VmConfig(vmConfigPath, vmJsonUtils.load(vmConfigPath));
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
