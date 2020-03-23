package id.jnix.tests.net.iptables;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.jnix.net.iptables.Chain;
import id.jnix.net.iptables.IpTables;
import id.jnix.net.iptables.Rule;
import id.jnix.net.iptables.Table;
import id.jnix.net.iptables.Target;
import id.xfunction.XProcess;

public class IpTablesTests {

	private List<String> out = new ArrayList<>();
	private IpTables iptables = new IpTables() {
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
    	iptables.add(new Rule(Chain.FORWARD, Target.ACCEPT)
    			.withInIface("tap0")
    			.withOutIface("eth0"));
		assertEquals("sudo iptables -A FORWARD -i tap0 -o eth0 -j ACCEPT", out.get(0));
    }

    @Test
    public void test_add_to_table() throws Exception {
    	iptables.add(new Rule(Chain.POSTROUTING, Target.MASQUERADE)
    			.withTable(Table.NAT)
    			.withOutIface("eth0"));
		assertEquals("sudo iptables -t NAT -A POSTROUTING -o eth0 -j MASQUERADE", out.get(0));
    }
}
