/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.tests.net.ip;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetAddress;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import id.jnix.net.ip.Address;
import id.jnix.net.ip.Ip;
import id.jnix.net.ip.Ip.Status;
import id.jnix.net.ip.Ip.TunnelMode;

public class IpTests {

    @Test
    public void test_address() throws Exception {
        var addrs = new Ip()
                .address();
        assertTrue(addrs.stream()
                .map(Address::getDeviceName)
                .anyMatch(Predicate.isEqual("lo")));
    }

    @Test
    public void test_happy() throws Exception {
        String ifaceName = "taptest1";
        Ip ip = new Ip().withSudo();
        ip.tunTapAdd(ifaceName, TunnelMode.tap);
        ip.addressAdd(ifaceName, InetAddress.getByName("172.16.2.1"));
        ip.linkSet(ifaceName, Status.up);
        ip.tunTapDel(ifaceName, TunnelMode.tap);
    }
}
