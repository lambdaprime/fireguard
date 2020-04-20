/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.tests.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import id.fireguard.net.MacAddress;

public class MacAddressTests {

    @Test
    public void test_toString() throws Exception {
        assertEquals("AA:FC:04:03:02:01", new MacAddress(0xAAFC04030201L).toString());
        assertEquals("FF:FF:FF:FF:FF:FF", new MacAddress(0xffffffffffffL).toString());
        assertEquals("10:01:FF:20:0C:00", new MacAddress(0x1001ff200c00L).toString());
        assertEquals("10:00:00:00:00:00", new MacAddress(0x100000000000L).toString());
    }

    @Test
    public void test_inc() throws Exception {
        assertEquals("AA:FC:04:03:02:02", new MacAddress(0xAAFC04030201L).inc().toString());
        assertEquals("00:00:00:00:00:00", new MacAddress(0xffffffffffffL).inc().toString());
        assertEquals("10:01:FF:20:0C:10", new MacAddress(0x1001ff200c0fL).inc().toString());
        assertEquals("10:00:00:00:10:00", new MacAddress(0x100000000fffL).inc().toString());
    }

    @Test
    public void test_parseMac() throws Exception {
        assertEquals(0xAAFC04030201L, MacAddress.parseMac("AA:FC:04:03:02:01").getValue());
        assertEquals(0xffffffffffffL, MacAddress.parseMac("FF:FF:FF:FF:FF:FF").getValue());
        assertEquals(0x1001ff200c00L, MacAddress.parseMac("10:01:FF:20:0C:00").getValue());
        assertEquals(0x100000000000L, MacAddress.parseMac("10:00:00:00:00:00").getValue());
    }
}
