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

/**
 * Represent network interface to which VM is attached on
 * the host system
 */
public class NetworkInterface {

    private String name;
    private String vmId;
    private InetAddress hostIp;
    private InetAddress vmIp;
    private MacAddress macAddress;
    private short mask = 24;

    public NetworkInterface(String name, String vmId, InetAddress hostIp, InetAddress vmIp, MacAddress macAddress) {
        this.name = name;
        this.vmId = vmId;
        this.hostIp = hostIp;
        this.vmIp = vmIp;
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public InetAddress getVmIp() {
        return vmIp;
    }

    public MacAddress getMac() {
        return macAddress;
    }

    public InetAddress getHostIp() {
        return hostIp;
    }

    public String getVmId() {
        return vmId;
    }

    public short getMask() {
        return mask;
    }
    
    @Override
    public String toString() {
        return format("{ name: %s, vmId: %s, hostIp: %s, vmIp: %s, macAddress: %s }",
                name, vmId, hostIp, vmIp, macAddress);
    }
}
