/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.net.dhcpd;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;

public class Host implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String mac;
    private InetAddress fixedAddress;
    private List<InetAddress> routers = List.of();
    private InetAddress subnetMask;
    private List<InetAddress> dns = List.of();

    public Host(String name, String mac, InetAddress fixedAddress) {
        this.name = name;
        this.mac = mac;
        this.fixedAddress = fixedAddress;
    }

    public Host withRouters(List<InetAddress> routers) {
        this.routers = routers;
        return this;
    }
    
    public Host withDns(List<InetAddress> dns) {
        this.dns = dns;
        return this;
    }
    
    public String getMac() {
        return mac;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        var buf = new StringBuilder();
        buf.append("hardware ethernet " + mac + ";\n");
        buf.append("fixed-address " + fixedAddress.getHostAddress() + ";\n");
        if (subnetMask != null) {
            buf.append("option subnet-mask  " + subnetMask.getHostAddress() + ";\n");
        }
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
