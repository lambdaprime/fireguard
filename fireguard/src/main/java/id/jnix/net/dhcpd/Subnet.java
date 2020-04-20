/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.net.dhcpd;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Subnet {

    InetAddress subnet;
    InetAddress netmask;
    List<Host> hosts = List.of();

    public Subnet(InetAddress subnet, InetAddress netmask, Host...hosts) {
        this.subnet = subnet;
        this.netmask = netmask;
        this.hosts = Arrays.asList(hosts);
    }

    @Override
    public String toString() {
        return String.format("subnet %s netmask %s { %s }", subnet.getHostAddress(),
                netmask.getHostAddress(),
                hosts.stream().map(Host::toString)
                .collect(Collectors.joining("\n")));
    }
}
