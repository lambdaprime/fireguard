/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
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

    public VirtualMachine(String id, State state, Path home, Path socket, VmConfig vmConfig) {
        this.id = id;
        this.state = state;
        this.home = home;
        this.socket = socket;
        this.vmConfig = vmConfig;
    }

    public VirtualMachine withPid(Long pid) {
        this.pid = Optional.of(pid);
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
