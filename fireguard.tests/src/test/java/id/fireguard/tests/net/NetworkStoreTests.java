/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.tests.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.fireguard.net.NetworkEntity;
import id.fireguard.net.NetworkStore;
import id.fireguard.tests.ObjectStoreMock;

public class NetworkStoreTests {

    private NetworkStore nm;

    @BeforeEach
    void setup() {
        nm = new NetworkStore(new ObjectStoreMock<>());
    }

    @Test
    public void test_update() throws UnknownHostException {
        String id = "net1";
        var net1 = new NetworkEntity(id, null, null);
        nm.add(net1);
        InetAddress subnet = InetAddress.getLocalHost();
        var net2 = new NetworkEntity(id, subnet, null);
        nm.update(net2);
        Assertions.assertEquals(subnet, nm.findNet(id).get().getSubnet());
    }

}
