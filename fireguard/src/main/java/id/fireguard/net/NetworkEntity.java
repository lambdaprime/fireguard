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

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;
import java.util.Set;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class NetworkEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    String id;
    InetAddress subnet;
    InetAddress netmask;
    Set<NetworkInterfaceEntity> ifaces = Set.of();

    public NetworkEntity(String id, InetAddress subnet, InetAddress netmask) {
        this.id = id;
        this.subnet = subnet;
        this.netmask = netmask;
    }

    public String getId() {
        return id;
    }

    public InetAddress getSubnet() {
        return subnet;
    }

    public InetAddress getNetmask() {
        return netmask;
    }

    public Set<NetworkInterfaceEntity> getIfaces() {
        return ifaces;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        NetworkEntity r = (NetworkEntity) obj;
        return Objects.equals(id, r.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
