package id.fireguard.net;

import java.io.Serializable;
import java.util.Optional;

import id.jnix.net.dhcpd.DhcpdConfig;
import id.xfunction.XObservable;

public class DhcpManagerConfig extends XObservable<DhcpManagerConfig> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Optional<Long> pid = Optional.empty();
	private DhcpdConfig dhcpdConfig = new DhcpdConfig();
	
	public Optional<Long> getPid() {
		return pid;
	}
	
	public DhcpdConfig getDhcpdConfig() {
		return dhcpdConfig;
	}

	public void updateDhcpdConfig(DhcpdConfig dhcpdConfig) {
		this.dhcpdConfig = dhcpdConfig;
		updateAll(this);
	}

	public void updatePid(long pid) {
		this.pid = Optional.of(pid);
		updateAll(this);
	}
}
