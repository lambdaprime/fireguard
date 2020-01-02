package id.fireguard.vmm.vmconfig;

import static java.lang.String.format;

import java.nio.file.Path;

public class VmConfig {

	private Path location;
	private int memoryGb, vcpu;
	private String ip;

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

	public String getIp() {
		return ip;
	}

	void setIp(String ip) {
		this.ip = ip;
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
		sb.append(format("ip: %s\n", ip));
		return sb.toString();
	}
}
