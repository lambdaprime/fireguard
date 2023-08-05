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
package id.jnix.tests.net.iptables;

import static org.junit.jupiter.api.Assertions.assertEquals;

import id.jnix.net.iptables.Chain;
import id.jnix.net.iptables.IpTables;
import id.jnix.net.iptables.Rule;
import id.jnix.net.iptables.Table;
import id.jnix.net.iptables.Target;
import id.xfunction.lang.XProcess;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IpTablesTests {

    private List<String> out = new ArrayList<>();
    private IpTables iptables =
            new IpTables() {
                @Override
                protected XProcess run(String cmd) {
                    out.add(cmd);
                    return new XProcess(null, Stream.of(), Stream.of(), 0);
                }
            };

    @BeforeEach
    void setup() {
        out.clear();
    }

    @Test
    public void test_add() throws Exception {
        iptables.add(
                new Rule(Chain.FORWARD, Target.ACCEPT).withInIface("tap0").withOutIface("eth0"));
        assertEquals("iptables -A FORWARD -i tap0 -o eth0 -j ACCEPT", out.get(0));
    }

    @Test
    public void test_add_to_table() throws Exception {
        iptables.add(
                new Rule(Chain.POSTROUTING, Target.MASQUERADE)
                        .withTable(Table.nat)
                        .withOutIface("eth0"));
        assertEquals("iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE", out.get(0));
    }
}
