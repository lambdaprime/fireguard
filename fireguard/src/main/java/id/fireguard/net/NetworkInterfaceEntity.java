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

/**
 * @author lambdaprime intid@protonmail.com
 */
public class NetworkInterfaceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    String id;
    String vmId;
    InetAddress hostIp;
    InetAddress vmIp;
    MacAddress macAddress;

    public String getVmId() {
        return vmId;
    }

    public InetAddress getHostIp() {
        return hostIp;
    }

    public InetAddress getVmIp() {
        return vmIp;
    }

    public MacAddress getMac() {
        return macAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        NetworkInterfaceEntity r = (NetworkInterfaceEntity) obj;
        return Objects.equals(id, r.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
