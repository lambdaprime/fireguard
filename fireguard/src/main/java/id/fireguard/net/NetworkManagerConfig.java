/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.net;

import java.io.Serializable;

import id.xfunction.XObservable;

public class NetworkManagerConfig extends XObservable<NetworkManagerConfig> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int lastNetId;
    private int lastIfaceId;
    private MacAddress lastUsedMacAddr  = new MacAddress(0);

    public MacAddress getLastUsedMacAddr() {
        return lastUsedMacAddr;
    }

    public void setLastUsedMacAddr(MacAddress lastUsedMacAddr) {
        this.lastUsedMacAddr = lastUsedMacAddr;
        updateAll(this);
    }

    public int getLastNetId() {
        return lastNetId;
    }

    public void setLastNetId(int lastId) {
        this.lastNetId = lastId;
        updateAll(this);
    }

    public int getLastIfaceId() {
        return lastIfaceId;
    }

    public void setLastIfaceId(int lastIfaceId) {
        this.lastIfaceId = lastIfaceId;
    }
}
