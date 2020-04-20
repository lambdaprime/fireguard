/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.tests.net;

import java.net.UnknownHostException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import id.fireguard.net.NetworkManagerConfig;

public class NetworkManagerConfigTests {

    @Test
    public void test_setLastId() throws UnknownHostException {
        boolean[] isNotified = new boolean[1];
        var nmc  = new NetworkManagerConfig();
        nmc.addListener(val -> {
            isNotified[0] = true;
        });
        nmc.setLastNetId(12);
        Assertions.assertTrue(isNotified[0]);
    }

    @Test
    public void test_setLastUsedMacAddr() throws UnknownHostException {
        boolean[] isNotified = new boolean[1];
        var nmc  = new NetworkManagerConfig();
        nmc.addListener(val -> {
            isNotified[0] = true;
        });
        nmc.setLastUsedMacAddr(null);
        Assertions.assertTrue(isNotified[0]);
    }
}
