/*
 * Copyright 2023 fireguard project
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
package id.jnix.net.dhcpd;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class SubnetSection implements Serializable {

    private static final long serialVersionUID = 1L;

    private InetAddress subnet;
    private InetAddress netmask;
    private List<Host> hosts = new ArrayList<>();

    public SubnetSection(InetAddress subnet, InetAddress netmask, Host... hosts) {
        this.subnet = subnet;
        this.netmask = netmask;
        this.hosts.addAll(Arrays.asList(hosts));
    }

    public InetAddress getSubnet() {
        return subnet;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void addHost(Host host) {
        hosts.add(host);
    }

    public InetAddress getNetmask() {
        return netmask;
    }

    @Override
    public String toString() {
        return String.format(
                "subnet %s netmask %s { %s }",
                subnet.getHostAddress(),
                netmask.getHostAddress(),
                hosts.stream().map(Host::toString).collect(Collectors.joining("\n")));
    }
}
