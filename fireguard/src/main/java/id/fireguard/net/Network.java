/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.net;

import static java.lang.String.format;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

public class Network {

    private String id;
    private InetAddress subnet;
    private InetAddress netmask;
    private Set<NetworkInterface> ifaces = new HashSet<>();

    public Network(String id, InetAddress subnet, InetAddress netmask) {
        this.id = id;
        this.subnet = subnet;
        this.netmask = netmask;
    }

    public Network withInterfaces(Set<NetworkInterface> ifaces) {
        this.ifaces = ifaces;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InetAddress getSubnet() {
        return subnet;
    }

    public void setSubnet(InetAddress subnet) {
        this.subnet = subnet;
    }

    public InetAddress getNetmask() {
        return netmask;
    }

    public void setNetmask(InetAddress netmask) {
        this.netmask = netmask;
    }

    public Set<NetworkInterface> getInterfaces() {
        return ifaces;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(format("id: %s\n", id));
        sb.append(format("subnet: %s\n", subnet));
        sb.append(format("netmask: %s\n", netmask));
        sb.append(format("ifaces: %s\n", ifaces));
        return sb.toString();
    }
}
