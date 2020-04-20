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
import java.util.Set;

public class NetworkEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    String id;
    InetAddress subnet;
    InetAddress netmask;
    Set<NetworkInterfaceEntity> ifaces;

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
