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
package id.fireguard.vmm.vmconfig;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class NetworkIface {

    private String ifaceId;
    private String mac;
    private String hostDevName;

    public NetworkIface(String ifaceId, String mac, String hostDevName) {
        this.ifaceId = ifaceId;
        this.mac = mac;
        this.hostDevName = hostDevName;
    }

    public String getIfaceId() {
        return ifaceId;
    }

    public void setIfaceId(String ifaceId) {
        this.ifaceId = ifaceId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getHostDevName() {
        return hostDevName;
    }

    public void setHostDevName(String hostDevName) {
        this.hostDevName = hostDevName;
    }
}
