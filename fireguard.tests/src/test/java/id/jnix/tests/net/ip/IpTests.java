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
package id.jnix.tests.net.ip;

import static org.junit.jupiter.api.Assertions.assertTrue;

import id.jnix.net.ip.Address;
import id.jnix.net.ip.Ip;
import id.jnix.net.ip.Ip.Status;
import id.jnix.net.ip.Ip.TunnelMode;
import id.jnix.net.ip.Route;
import java.net.InetAddress;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

public class IpTests {

    @Test
    public void test_address() throws Exception {
        var addrs = new Ip().address();
        assertTrue(addrs.stream().map(Address::getDeviceName).anyMatch(Predicate.isEqual("lo")));
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

    @Test
    public void test_route() throws Exception {
        Ip ip = new Ip().withSudo();
        var addr = InetAddress.getByName("11.16.2.1");
        var iface = "lo";
        Route route = new Route(addr.getHostAddress(), iface);
        ip.routeAdd(route);
        boolean isAdded = ip.route().stream().anyMatch(Predicate.isEqual(route));
        ip.routeDel(route);
        boolean isDeleted = ip.route().stream().noneMatch(Predicate.isEqual(route));
        assertTrue(isAdded);
        assertTrue(isDeleted);
    }
}
