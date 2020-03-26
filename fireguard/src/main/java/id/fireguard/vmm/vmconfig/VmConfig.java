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
        return json.getMachineConfig().map(m -> m.memory).orElse(0);
    }

    void setMemoryGb(int memoryGb) {
    	json.getMachineConfig().ifPresent(m -> m.memory = memoryGb);
    }

    public int getVcpu() {
    	return json.getMachineConfig().map(m -> m.vcpu).orElse(0);
    }

    void setVcpu(int vcpu) {
    	json.getMachineConfig().ifPresent(m -> m.vcpu = vcpu);
    }

    public Optional<String> getHostIface() {
    	return json.getNetworkInterfaces().map(i -> i.get(0).hostDevName);
    }

    void setHostIface(String hostIface) {
    	json.getNetworkInterfaces().ifPresent(i -> i.get(0).hostDevName = hostIface);
    }

    public Optional<String> getMac() {
    	return json.getNetworkInterfaces().map(i -> i.get(0).mac);
    }

    public void setMac(String mac) {
    	json.getNetworkInterfaces().ifPresent(i -> i.get(0).mac = mac);
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
