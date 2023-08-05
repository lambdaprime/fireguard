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

import id.fireguard.net.NetworkManagerConfig;
import java.net.UnknownHostException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NetworkManagerConfigTests {

    @Test
    public void test_setLastId() throws UnknownHostException {
        boolean[] isNotified = new boolean[1];
        var nmc = new NetworkManagerConfig();
        nmc.addListener(
                val -> {
                    isNotified[0] = true;
                });
        nmc.setLastNetId(12);
        Assertions.assertTrue(isNotified[0]);
    }

    @Test
    public void test_setLastUsedMacAddr() throws UnknownHostException {
        boolean[] isNotified = new boolean[1];
        var nmc = new NetworkManagerConfig();
        nmc.addListener(
                val -> {
                    isNotified[0] = true;
                });
        nmc.setLastUsedMacAddr(null);
        Assertions.assertTrue(isNotified[0]);
    }
}
