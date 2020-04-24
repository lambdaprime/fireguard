package id.fireguard.net;

import static java.lang.String.format;

import id.jnix.net.dhcpd.Dhcpd;
import id.jnix.net.dhcpd.DhcpdConfig;
import id.jnix.net.dhcpd.Host;
import id.jnix.net.dhcpd.SubnetSection;
import id.xfunction.XProcess;
import id.xfunction.XUtils;
import id.xfunction.function.Unchecked;

public class DhcpManager {

	private DhcpManagerConfig config;
	
	public DhcpManager(DhcpManagerConfig config) {
		this.config = config;
	}

	public void register(Network network, NetworkInterface iface) {
		DhcpdConfig dhcpdConfig = config.getDhcpdConfig();
		var subnetSection = dhcpdConfig.getSubnet(network.getSubnet()).orElseGet(() -> {
			var s = new SubnetSection(network.getSubnet(), network.getNetmask());
			dhcpdConfig.addSubnet(s);
			return s;
		});
		var host = new Host(iface.getName(), iface.getMac().toString(), iface.getVmIp());
		subnetSection.addHost(host);
		config.updateDhcpdConfig(dhcpdConfig);
		restart();
	}

	private void restart() {
		DhcpdConfig dhcpdConfig = config.getDhcpdConfig();
		var ph = config.getPid()
				.flatMap(ProcessHandle::of);
		if (ph.isPresent()) {
			ph.get().destroyForcibly();
		}
		var proc = new XProcess(Unchecked.get(() -> new Dhcpd().start(dhcpdConfig)));
		if (proc.getCode() != 0) {
			XUtils.throwRuntime(format("Command to start dhcpd %s failed with an error: %s",
					proc.process().info().commandLine().orElse(""),
					proc.stderrAsString() + proc.stdoutAsString()));
		}
		config.updatePid(proc.process().pid());
	}
}
