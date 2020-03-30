package id.fireguard.net;

import java.io.Serializable;

import id.xfunction.XObservable;

public class NetworkManagerConfig extends XObservable<NetworkManagerConfig> implements Serializable {

	private static final long serialVersionUID = 1L;

	private MacAddress lastUsedMacAddr  = new MacAddress(0);

	public MacAddress getLastUsedMacAddr() {
		return lastUsedMacAddr;
	}

	public void setLastUsedMacAddr(MacAddress lastUsedMacAddr) {
		this.lastUsedMacAddr = lastUsedMacAddr;
		notifyAll(this);
	}

}
