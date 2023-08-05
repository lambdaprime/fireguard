/*
 * Copyright 2020 fireguard project
 * 
 * Website: https://github.com/lambdaprime/fireguard
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.fireguard.vmm;

import static java.lang.String.format;

import id.fireguard.vmm.vmconfig.VmConfig;
import java.nio.file.Path;
import java.util.Optional;

/**
 * @author lambdaprime intid@protonmail.com
 */
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
