package id.fireguard.tests.net.generators;

import static java.net.InetAddress.getByName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetAddress;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import id.fireguard.net.generators.IpGenerator;

public class IpGeneratorTests extends IpGenerator {

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
    	Map<InetAddress, Set<InetAddress>> ipPool = Map.of(
    			getByName("123.2.3.0"), Set.of(getByName("123.2.3.254")));
    	var actual = nextIp(ipPool, getByName("123.2.3.0")).get().toString();
    	assertEquals("/123.2.3.1", actual);
    }

    @Test
    public void test_nextIp_wrong_subnet() throws Exception {
    	Map<InetAddress, Set<InetAddress>> ipPool = Map.of(
    			getByName("123.2.3.0"), Set.of(getByName("123.2.3.254")));
    	Assertions.assertThrows(AssertionError.class,
				() -> nextIp(ipPool, getByName("123.2.3.12")));
    }

    @Test
    public void test_nextIp_after_1() throws Exception {
    	Map<InetAddress, Set<InetAddress>> ipPool = Map.of(
    			getByName("123.2.3.0"), Set.of(
    					getByName("123.2.3.1"),
    					getByName("123.2.3.254")));
    	var actual = nextIp(ipPool, getByName("123.2.3.0")).get().toString();
    	assertEquals("/123.2.3.2", actual);
    }

    @Test
    public void test_nextIp_consecutive() throws Exception {
    	Map<InetAddress, Set<InetAddress>> ipPool = Map.of(
    			getByName("123.2.3.0"), Set.of(
    					getByName("123.2.3.4"),
    					getByName("123.2.3.3"),
    					getByName("123.2.3.2"),
    					getByName("123.2.3.1")));
    	var actual = nextIp(ipPool, getByName("123.2.3.0")).get().toString();
    	assertEquals("/123.2.3.5", actual);
    }
}
