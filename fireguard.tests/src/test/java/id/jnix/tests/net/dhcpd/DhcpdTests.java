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
package id.jnix.tests.net.dhcpd;

import static java.net.InetAddress.getByName;
import static org.junit.jupiter.api.Assertions.assertEquals;

import id.jnix.net.dhcpd.Dhcpd;
import id.jnix.net.dhcpd.DhcpdConfig;
import id.jnix.net.dhcpd.Host;
import id.jnix.net.dhcpd.SubnetSection;
import id.xfunction.ResourceUtils;
import id.xfunction.lang.XProcess;
import org.junit.jupiter.api.Test;

public class DhcpdTests {
    private static final ResourceUtils utils = new ResourceUtils();

    @Test
    public void test_config() throws Exception {
        DhcpdConfig conf =
                new DhcpdConfig(
                        new SubnetSection(
                                getByName("172.16.0.0"),
                                getByName("255.255.0.0"),
                                new Host("vm1", "AA:FC:00:00:00:01", getByName("172.16.1.10"))));
        assertEquals(utils.readResource(getClass(), "dhcpd.config"), conf.toString());
    }

    public static void main(String[] args) throws Exception {
        DhcpdConfig conf =
                new DhcpdConfig(
                        new SubnetSection(
                                getByName("172.16.0.0"),
                                getByName("255.255.0.0"),
                                new Host("vm1", "AA:FC:00:00:00:01", getByName("172.16.1.10"))));
        XProcess proc = new XProcess(new Dhcpd().start(conf));
        System.out.println(proc.stderr());
    }
}
