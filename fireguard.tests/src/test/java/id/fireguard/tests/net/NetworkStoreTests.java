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
package id.fireguard.tests.net;

import id.fireguard.net.NetworkEntity;
import id.fireguard.net.NetworkStore;
import id.fireguard.tests.ObjectStoreMock;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
