/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.net;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;

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
