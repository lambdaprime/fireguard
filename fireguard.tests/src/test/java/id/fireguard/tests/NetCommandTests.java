/*
 * Copyright 2020 fireguard project
 * 
 * Website: https://github.com/lambdaprime/fireguard
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.fireguard.tests;

import id.fireguard.Settings;
import id.fireguard.app.CommandIllegalArgumentException;
import id.fireguard.app.NetCommand;
import id.fireguard.net.NetworkInstaller;
import id.fireguard.net.NetworkManager;
import id.fireguard.net.NetworkManagerConfig;
import id.fireguard.net.NetworkStore;
import id.fireguard.net.NetworkTransformer;
import id.xfunction.cli.CommandLineInterface;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NetCommandTests {

    private NetCommand nc;
    private NetworkManager nm;

    static class NetworkManagerMock extends NetworkManager {
        protected NetworkManagerMock() {
            super(
                    new NetworkManagerConfig(),
                    new NetworkStore(new ObjectStoreMock<>()),
                    new NetworkTransformer(),
                    new NetworkInstaller(null, new Settings()));
        }
    }

    @BeforeEach
    void setup() {
        nm = new NetworkManagerMock();
        nc = new NetCommand(nm, CommandLineInterface.cli);
    }

    @Test
    public void test_execute_noargs() {
        Assertions.assertThrows(
                CommandIllegalArgumentException.class, () -> nc.execute(new LinkedList<>()));
    }

    @Test
    public void test_create_noargs() {
        List<String> args = new LinkedList<>(List.of("create"));
        Assertions.assertThrows(CommandIllegalArgumentException.class, () -> nc.execute(args));
    }

    @Test
    public void test_create_uniq_subnets() throws Exception {
        List<String> args = List.of("create", "10.1.1.0", "255.255.0.0");
        nc.execute(new LinkedList<>(args));
        Assertions.assertThrows(RuntimeException.class, () -> nc.execute(new LinkedList<>(args)));
    }
}
