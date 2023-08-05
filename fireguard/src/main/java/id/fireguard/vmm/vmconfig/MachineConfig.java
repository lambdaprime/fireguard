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
package id.fireguard.vmm.vmconfig;

/**
 * @author lambdaprime intid@protonmail.com
 */
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
