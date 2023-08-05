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
package id.fireguard.net;

import static java.lang.String.format;

import java.net.InetAddress;

/** Represent network interface to which VM is attached on the host system */
/**
 * @author lambdaprime intid@protonmail.com
 */
public class NetworkInterface {

    private String name;
    private String vmId;
    private InetAddress hostIp;
    private InetAddress vmIp;
    private MacAddress macAddress;
    private short mask = 24;

    public NetworkInterface(
            String name, String vmId, InetAddress hostIp, InetAddress vmIp, MacAddress macAddress) {
        this.name = name;
        this.vmId = vmId;
        this.hostIp = hostIp;
        this.vmIp = vmIp;
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public InetAddress getVmIp() {
        return vmIp;
    }

    public MacAddress getMac() {
        return macAddress;
    }

    public InetAddress getHostIp() {
        return hostIp;
    }

    public String getVmId() {
        return vmId;
    }

    public short getMask() {
        return mask;
    }

    @Override
    public String toString() {
        return format(
                "{ name: %s, vmId: %s, hostIp: %s, vmIp: %s, macAddress: %s }",
                name, vmId, hostIp, vmIp, macAddress);
    }
}
