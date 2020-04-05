package id.fireguard.vmm.vmconfig;

public class MachineConfig {

	private int vcpu;
	private int memory;
	
	public MachineConfig(int vcpu, int memory) {
		this.vcpu = vcpu;
		this.memory = memory;
	}
	
	public int getMemory() {
		return memory;
	}
	
	public int getVcpu() {
		return vcpu;
	}
	
	public void setMemory(int memory) {
		this.memory = memory;
	}
	
	public void setVcpu(int vcpu) {
		this.vcpu = vcpu;
	}
}
