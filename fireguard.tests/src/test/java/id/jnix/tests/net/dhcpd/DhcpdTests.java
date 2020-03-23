package id.jnix.tests.net.dhcpd;

import static java.net.InetAddress.getByName;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import id.jnix.net.dhcpd.Dhcpd;
import id.jnix.net.dhcpd.DhcpdConfig;
import id.jnix.net.dhcpd.Host;
import id.jnix.net.dhcpd.Subnet;
import id.xfunction.XProcess;
import id.xfunction.XUtils;

public class DhcpdTests {

    @Test
    public void test_config() throws Exception {
		DhcpdConfig conf = new DhcpdConfig(new Subnet(getByName("172.16.0.0"),
				getByName("255.255.0.0"),
				new Host("vm1", "AA:FC:00:00:00:01", getByName("172.16.1.10"))));
		assertEquals(XUtils.readResource(getClass(), "dhcpd.config"), conf.toString());
    }

    public static void main(String[] args) throws Exception {
		DhcpdConfig conf = new DhcpdConfig(new Subnet(getByName("172.16.0.0"),
				getByName("255.255.0.0"),
				new Host("vm1", "AA:FC:00:00:00:01", getByName("172.16.1.10"))));
		XProcess proc = new XProcess(new Dhcpd().start(conf));
		System.out.println(proc.stderrAsString());
	}
}
