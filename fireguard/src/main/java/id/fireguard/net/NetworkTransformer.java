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

import java.util.stream.Collectors;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class NetworkTransformer {

    public NetworkInterfaceEntity toEntity(NetworkInterface n) {
        var ne = new NetworkInterfaceEntity();
        ne.id = n.getName();
        ne.vmId = n.getVmId();
        ne.hostIp = n.getHostIp();
        ne.vmIp = n.getVmIp();
        ne.macAddress = n.getMac();
        return ne;
    }

    public NetworkEntity toEntity(Network n) {
        var ne = new NetworkEntity(n.getId(), n.getSubnet(), n.getNetmask());
        ne.ifaces = n.getInterfaces().stream().map(this::toEntity).collect(Collectors.toSet());
        return ne;
    }

    public NetworkInterface fromEntity(NetworkInterfaceEntity ne) {
        var iface =
                new NetworkInterface(
                        ne.getId(), ne.getVmId(), ne.getHostIp(), ne.getVmIp(), ne.getMac());
        return iface;
    }

    public Network fromEntity(NetworkEntity ne) {
        var net = new Network(ne.id, ne.subnet, ne.netmask);
        if (ne.ifaces != null)
            net.withInterfaces(
                    ne.ifaces.stream().map(this::fromEntity).collect(Collectors.toSet()));
        return net;
    }
}
