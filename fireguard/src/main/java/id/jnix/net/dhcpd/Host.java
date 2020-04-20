/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.net.dhcpd;

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Host {

    String name;
    String mac;
    InetAddress fixedAddress;
    List<InetAddress> routers = List.of();
    Optional<InetAddress> subnetMask = Optional.empty();
    List<InetAddress> dns = List.of();

    public Host(String name, String mac, InetAddress fixedAddress) {
        this.name = name;
        this.mac = mac;
        this.fixedAddress = fixedAddress;
    }

    @Override
    public String toString() {
        var buf = new StringBuilder();
        buf.append("hardware ethernet " + mac + ";\n");
        buf.append("fixed-address " + fixedAddress.getHostAddress() + ";\n");
        subnetMask.ifPresent(ip -> {
            buf.append("option subnet-mask  " + ip.getHostAddress() + ";\n");
        });
        if (!routers.isEmpty() ) {
            buf.append("option routers " + routers.stream().map(InetAddress::getHostAddress)
                    .collect(Collectors.joining(",")) + ";\n");
        }
        if (!dns.isEmpty() ) {
            buf.append("option domain-name-servers " + dns.stream().map(InetAddress::getHostAddress)
                    .collect(Collectors.joining(",")) + ";\n");
        }
        return String.format("host %s { %s }", name, buf.toString());
    }
}
