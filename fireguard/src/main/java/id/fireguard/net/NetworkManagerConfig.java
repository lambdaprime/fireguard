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
package id.fireguard.net;

import id.xfunction.XObservable;
import java.io.Serializable;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class NetworkManagerConfig extends XObservable<NetworkManagerConfig>
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private int lastNetId;
    private int lastIfaceId;
    private MacAddress lastUsedMacAddr = new MacAddress(0);

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
