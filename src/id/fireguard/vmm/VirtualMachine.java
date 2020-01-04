package id.fireguard.vmm;

import static java.lang.String.format;

import java.nio.file.Path;
import java.util.Optional;

import id.fireguard.vmm.vmconfig.VmConfig;

public class VirtualMachine {

	private String id;
	private State state;
	private Path home, socket;
	private VmConfig vmConfig;
	private Optional<Long> pid = Optional.empty();

	public VirtualMachine withId(String id) {
		this.id = id;
		return this;
	}

	public VirtualMachine withHome(Path home) {
		this.home = home;
		return this;
	}

	public VirtualMachine withSocket(Path socket) {
		this.socket = socket;
		return this;
	}

	public VirtualMachine withVmConfig(VmConfig vmConfig) {
		this.vmConfig = vmConfig;
		return this;
	}

	public VirtualMachine withState(State state) {
		this.state = state;
		return this;
	}

	public VirtualMachine withPid(Long pid) {
		this.pid = Optional.ofNullable(pid);
		return this;
	}

	public Path getHome() {
		return home;
	}

	public Path getSocket() {
		return socket;
	}

	public String getId() {
		return id;
	}

	public State getState() {
		return state;
	}

	public VmConfig getVmConfig() {
		return vmConfig;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Optional<Long> getPid() {
		return pid;
	}

	public void setPid(Optional<Long> pid) {
		this.pid = pid;
	}

	@Override
	public String toString() {
		var sb = new StringBuilder();
		sb.append(format("id: %s\n", id));
		sb.append(format("home folder: %s\n", home));
		sb.append(format("socket: %s\n", socket));
		sb.append(format("state: %s\n", state));
		sb.append(format("pid: %s\n", pid));
		sb.append(format("vmConfig: %s\n", vmConfig));
		return sb.toString();
	}

}
