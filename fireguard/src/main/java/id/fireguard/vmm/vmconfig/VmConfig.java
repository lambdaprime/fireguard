package id.fireguard.vmm.vmconfig;

import static java.lang.String.format;

import java.nio.file.Path;
import java.util.Optional;

public class VmConfig {

    private Path location;
    private VmConfigJson json;

    VmConfig(Path location, VmConfigJson json) {
        this.location = location;
        this.json = json;
    }

    public int getMemoryGb() {
        return json.getMachineConfig().getMemory();
    }

    void setMemoryGb(int memoryGb) {
    	json.getMachineConfig().setMemory(memoryGb);
    }

    public int getVcpu() {
    	return json.getMachineConfig().getVcpu();
    }

    void setVcpu(int vcpu) {
    	json.getMachineConfig().setVcpu(vcpu);
    }

    public Optional<String> getHostIface() {
    	return json.getNetworkInterfaces().stream()
    			.findFirst()
    			.map(i -> i.getHostDevName());
    }

    void setHostIface(String hostIface) {
    	json.getNetworkInterfaces().stream()
    		.findFirst()
    		.ifPresent(i -> i.setHostDevName(hostIface));
    }

    public Optional<String> getMac() {
    	return json.getNetworkInterfaces().stream()
    			.findFirst()
    			.map(i -> i.getMac());
    }

    public void setMac(String mac) {
    	json.getNetworkInterfaces().stream()
    		.findFirst()
    		.ifPresent(i -> i.setMac(mac));
    }

    public Path getLocation() {
        return location;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(format("path: %s\n", location.toString()));
        sb.append(format("memoryGb: %s\n", getMemoryGb()));
        sb.append(format("vcpu: %s\n", getVcpu()));
        sb.append(format("hostIface: %s\n", getHostIface()));
        sb.append(format("mac: %s\n", getMac()));
        return sb.toString();
    }
}
