/*
 * Copyright 2023 fireguard project
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

import id.jnix.net.dhcpd.Dhcpd;
import id.jnix.net.dhcpd.DhcpdConfig;
import id.jnix.net.dhcpd.Host;
import id.jnix.net.dhcpd.SubnetSection;
import id.xfunction.function.Unchecked;
import java.net.InetAddress;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class DhcpManager {

    private DhcpManagerConfig config;

    public DhcpManager(DhcpManagerConfig config) {
        this.config = config;
    }

    public boolean isPresent(Network network, NetworkInterface iface) {
        DhcpdConfig dhcpdConfig = config.getDhcpdConfig();
        var subnetSection = dhcpdConfig.getSubnet(network.getSubnet());
        return subnetSection.map(SubnetSection::getHosts).stream()
                .flatMap(List::stream)
                .map(Host::getMac)
                .anyMatch(Predicate.isEqual(iface.getMac().toString()));
    }

    public void register(Network network, NetworkInterface iface) {
        DhcpdConfig dhcpdConfig = config.getDhcpdConfig();
        var subnetSection =
                dhcpdConfig
                        .getSubnet(network.getSubnet())
                        .orElseGet(
                                () -> {
                                    var s =
                                            new SubnetSection(
                                                    network.getSubnet(), network.getNetmask());
                                    dhcpdConfig.addSubnet(s);
                                    return s;
                                });
        var host =
                new Host(iface.getName(), iface.getMac().toString(), iface.getVmIp())
                        .withRouters(List.of(iface.getHostIp()))
                        .withDns(
                                Unchecked.get(
                                        () ->
                                                List.of(
                                                        InetAddress.getByName("8.8.4.4"),
                                                        InetAddress.getByName("8.8.8.8"))));
        subnetSection.addHost(host);
        config.updateDhcpdConfig(dhcpdConfig);
    }

    public void stop() {
        Unchecked.run(() -> new Dhcpd().stop());
    }

    public boolean isRunning() {
        return Unchecked.get(() -> new Dhcpd().isRunning());
    }

    public void start() {
        DhcpdConfig dhcpdConfig = config.getDhcpdConfig();
        Unchecked.run(() -> new Dhcpd().start(dhcpdConfig));
    }

    public void restart() {
        stop();
        start();
    }
}
