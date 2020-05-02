package id.fireguard.net;

import java.io.Serializable;

import id.jnix.net.dhcpd.DhcpdConfig;
import id.xfunction.XObservable;

public class DhcpManagerConfig extends XObservable<DhcpManagerConfig> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DhcpdConfig dhcpdConfig = new DhcpdConfig();
	
	public DhcpdConfig getDhcpdConfig() {
		return dhcpdConfig;
	}

	public void updateDhcpdConfig(DhcpdConfig dhcpdConfig) {
		this.dhcpdConfig = dhcpdConfig;
		updateAll(this);
	}
}
