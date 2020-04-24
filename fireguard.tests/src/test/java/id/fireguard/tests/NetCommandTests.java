/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.tests;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.fireguard.app.CommandIllegalArgumentException;
import id.fireguard.app.NetCommand;
import id.fireguard.net.NetworkManager;
import id.fireguard.net.NetworkManagerConfig;
import id.fireguard.net.NetworkStore;
import id.fireguard.net.NetworkTransformer;

public class NetCommandTests {

    private NetCommand nc;
    private NetworkManager nm;

    static class NetworkManagerMock extends NetworkManager {
        protected NetworkManagerMock() {
            super(new NetworkManagerConfig(),
                    new NetworkStore(new ObjectStoreMock<>()),
                    new NetworkTransformer());
        }
    }

    @BeforeEach
    void setup() {
        nm = new NetworkManagerMock();
        nc = new NetCommand(nm);
    }

    @Test
    public void test_execute_noargs() {
        Assertions.assertThrows(CommandIllegalArgumentException.class,
                () -> nc.execute(new LinkedList<>()));
    }

    @Test
    public void test_create_noargs() {
        List<String> args = new LinkedList<>(List.of("create"));
        Assertions.assertThrows(CommandIllegalArgumentException.class,
                () -> nc.execute(args));
    }

    @Test
    public void test_create_uniq_subnets() throws Exception {
        List<String> args = List.of(
                "create", "10.1.1.0", "255.255.0.0");
        nc.execute(new LinkedList<>(args));
        Assertions.assertThrows(RuntimeException.class,
                () -> nc.execute(new LinkedList<>(args)));
    }
}
