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

import static java.net.InetAddress.getByName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import id.fireguard.net.IpPool;
import java.net.InetAddress;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class IpPoolTests extends IpPool {

    public IpPoolTests() {
        super(Set.of());
    }

    @Test
    public void test_inc() throws Exception {
        String actual = inc(getByName("123.2.3.0")).get().toString();
        assertEquals("/123.2.3.1", actual);

        actual = inc(getByName("123.2.3.254")).get().toString();
        assertEquals("/123.2.4.1", actual);

        actual = inc(getByName("123.2.254.254")).get().toString();
        assertEquals("/123.3.1.1", actual);

        actual = inc(getByName("123.2.254.253")).get().toString();
        assertEquals("/123.2.254.254", actual);

        actual = inc(getByName("254.254.254.254")).map(Object::toString).orElse(null);
        assertEquals(null, actual);
    }

    @Test
    public void test_cmpIp() throws Exception {
        assertEquals(1, cmpIp(getByName("123.2.3.254"), getByName("123.2.3.253")));
        assertEquals(0, cmpIp(getByName("123.2.3.254"), getByName("123.2.3.254")));
        assertEquals(-256, cmpIp(getByName("123.2.4.3"), getByName("123.2.5.3")));
    }

    @Test
    public void test_valid() throws Exception {
        assertTrue(valid(getByName("123.2.3.0"), getByName("123.2.3.254")));
        assertTrue(valid(getByName("123.2.0.0"), getByName("123.2.3.254")));
        assertFalse(valid(getByName("123.2.3.0"), getByName("123.2.5.254")));
    }

    @Test
    public void test_nextIp_from_1() throws Exception {
        Set<InetAddress> ipPool = Set.of(getByName("123.2.3.254"));
        var actual = new IpPool(ipPool).newIp(getByName("123.2.3.0")).get().toString();
        assertEquals("/123.2.3.1", actual);
    }

    @Test
    public void test_nextIp_after_1() throws Exception {
        Set<InetAddress> ipPool = Set.of(getByName("123.2.3.1"), getByName("123.2.3.254"));
        var actual = new IpPool(ipPool).newIp(getByName("123.2.3.0")).get().toString();
        assertEquals("/123.2.3.2", actual);
    }

    @Test
    public void test_nextIp_consecutive() throws Exception {
        Set<InetAddress> ipPool =
                Set.of(
                        getByName("123.2.3.4"),
                        getByName("123.2.3.3"),
                        getByName("123.2.3.2"),
                        getByName("123.2.3.1"));
        var actual = new IpPool(ipPool).newIp(getByName("123.2.3.0")).get().toString();
        assertEquals("/123.2.3.5", actual);
    }

    @Test
    public void test_nextIp_multiple_calls() throws Exception {
        Set<InetAddress> ipPool =
                Set.of(
                        getByName("123.2.3.4"),
                        getByName("123.2.3.3"),
                        getByName("123.2.3.2"),
                        getByName("123.2.3.1"));
        var generator = new IpPool(ipPool);
        var actual =
                List.of(
                        generator.newIp(getByName("123.2.3.0")).get().toString(),
                        generator.newIp(getByName("123.2.3.0")).get().toString());
        assertEquals("[/123.2.3.5, /123.2.3.6]", actual.toString());
    }
}
