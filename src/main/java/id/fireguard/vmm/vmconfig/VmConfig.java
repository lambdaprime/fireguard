package id.fireguard.vmm.vmconfig;

import static java.lang.String.format;

import java.nio.file.Path;
import java.util.Optional;

public class VmConfig {

    private Path location;
    private int memoryGb, vcpu;
    private Optional<String> hostIface = Optional.empty();
    private Optional<String> mac = Optional.empty();

    VmConfig(Path location) {
        this.location = location;
    }

    public int getMemoryGb() {
        return memoryGb;
    }

    void setMemoryGb(int memoryGb) {
        this.memoryGb = memoryGb;
    }

    public int getVcpu() {
        return vcpu;
    }

    void setVcpu(int vcpu) {
        this.vcpu = vcpu;
    }

    public Optional<String> getHostIface() {
        return hostIface;
    }

    void setHostIface(String hostIface) {
        this.hostIface = Optional.of(hostIface);
    }

    public Optional<String> getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac =  Optional.of(mac);
    }

    public Path getLocation() {
        return location;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(format("path: %s\n", location.toString()));
        sb.append(format("memoryGb: %s\n", memoryGb));
        sb.append(format("vcpu: %s\n", vcpu));
        sb.append(format("hostIface: %s\n", hostIface));
        sb.append(format("mac: %s\n", mac));
        return sb.toString();
    }
}
