package id.fireguard.vmm.vmconfig;

import static java.lang.String.format;

public class VmConfigJson2 {

	private int memoryGb, vcpu;
	private String bootArgs, logFifo, kernelImagePath, pathOnHost, ip;

	VmConfigJson2() {}

	public int getMemoryGb() {
		return memoryGb;
	}

	public void setMemoryGb(int memoryGb) {
		this.memoryGb = memoryGb;
	}

	public int getVcpu() {
		return vcpu;
	}

	public void setVcpu(int vcpu) {
		this.vcpu = vcpu;
	}

	public String getBootArgs() {
		return bootArgs;
	}

	public void setBootArgs(String bootArgs) {
		this.bootArgs = bootArgs;
	}

	public String getLogFifo() {
		return logFifo;
	}

	public void setLogFifo(String logFifo) {
		this.logFifo = logFifo;
	}

	public String getKernelImagePath() {
		return kernelImagePath;
	}

	public void setKernelImagePath(String kernelImagePath) {
		this.kernelImagePath = kernelImagePath;
	}

	public String getPathOnHost() {
		return pathOnHost;
	}

	public void setPathOnHost(String pathOnHost) {
		this.pathOnHost = pathOnHost;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		var sb = new StringBuilder();
		sb.append(format("memoryGb: %s\n", memoryGb));
		sb.append(format("vcpu: %s\n", vcpu));
		sb.append(format("bootArgs: %s\n", bootArgs));
		sb.append(format("logFifo: %s\n", logFifo));
		sb.append(format("logFifo: %s\n", kernelImagePath));
		sb.append(format("logFifo: %s\n", pathOnHost));
		sb.append(format("logFifo: %s\n", ip));
		return sb.toString();
	}
}
