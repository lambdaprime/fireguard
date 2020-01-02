package id.fireguard.vmm;

import java.nio.file.Path;

import id.fireguard.vmm.vmconfig.VmConfig;
import id.fireguard.vmm.vmconfig.VmConfigUtils;

public class VirtualMachineBuilder {

	private VmConfigUtils configUtils = new VmConfigUtils();
	
	public VirtualMachine build(String id, State state, Path home, Path socket) {
		VmConfig vmConfig = configUtils.load(home);
		return new VirtualMachine()
				.withId(id)
				.withHome(home)
				.withSocket(socket)
				.withVmConfig(vmConfig)
				.withState(state);
	}
}
