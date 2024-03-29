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

import static id.jnix.net.iptables.Conntrack.State.ESTABLISHED;
import static id.jnix.net.iptables.Conntrack.State.RELATED;

import id.fireguard.Settings;
import id.jnix.CommandExecutionException;
import id.jnix.ProcFs;
import id.jnix.net.ip.Address;
import id.jnix.net.ip.Ip;
import id.jnix.net.ip.Ip.Status;
import id.jnix.net.ip.Ip.TunnelMode;
import id.jnix.net.ip.Route;
import id.jnix.net.iptables.Chain;
import id.jnix.net.iptables.Conntrack;
import id.jnix.net.iptables.IpTables;
import id.jnix.net.iptables.Rule;
import id.jnix.net.iptables.Table;
import id.jnix.net.iptables.Target;
import id.xfunction.Preconditions;
import id.xfunction.function.Unchecked;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class NetworkInstaller {

    private DhcpManager dhcpManager;
    private Settings settings;

    public NetworkInstaller(DhcpManager dhcpManager, Settings settings) {
        this.dhcpManager = dhcpManager;
        this.settings = settings;
    }

    public boolean exist(String ifaceId) {
        return Unchecked.getBoolean(
                () ->
                        new Ip()
                                .address().stream()
                                        .map(Address::getDeviceName)
                                        .anyMatch(Predicate.isEqual(ifaceId)));
    }

    public void setup(Network network, NetworkInterface iface) {
        Unchecked.run(() -> verify());
        Unchecked.getBoolean(() -> createIface(iface));
        Unchecked.run(() -> setupRouting(iface));
        Unchecked.run(() -> configureNat(iface));
        // update DHCP
        if (!dhcpManager.isPresent(network, iface)) {
            dhcpManager.register(network, iface);
        }
        dhcpManager.restart();
    }

    private void setupRouting(NetworkInterface iface) throws CommandExecutionException {
        var ip = new Ip().withSudo();
        var routeToHost = new Route(iface.getHostIp().getHostAddress(), iface.getName());
        List<Route> routes = ip.route();
        if (routes.stream().noneMatch(Predicate.isEqual(routeToHost))) {
            System.out.println("Adding new route: " + routeToHost);
            ip.routeAdd(routeToHost);
        }
        var routeToVm = new Route(iface.getVmIp().getHostAddress(), iface.getName());
        if (routes.stream().noneMatch(Predicate.isEqual(routeToVm))) {
            ip.routeAdd(routeToVm);
            System.out.println("Adding new route: " + routeToVm);
        }
    }

    private void configureNat(NetworkInterface iface) throws CommandExecutionException {
        // setup NAT
        var iptables = new IpTables().withSudo();
        Rule[] rules = {
            new Rule(Chain.POSTROUTING, Target.MASQUERADE)
                    .withTable(Table.nat)
                    .withOutIface(settings.getHostIface()),
            new Rule(Chain.FORWARD, Target.ACCEPT)
                    .withModule(new Conntrack().withStates(List.of(RELATED, ESTABLISHED))),
        };
        for (Rule r : rules) {
            if (!iptables.isPresent(r)) iptables.add(r);
        }
    }

    private boolean createIface(NetworkInterface iface) throws CommandExecutionException {
        if (exist(iface.getName())) {
            return false;
        }
        System.out.println("Creating network device " + iface.getName());
        var ip = new Ip().withSudo();
        ip.tunTapAdd(iface.getName(), TunnelMode.tap);
        ip.addressAdd(iface.getName(), iface.getHostIp());
        ip.linkSet(iface.getName(), Status.up);
        return true;
    }

    private void verify() throws CommandExecutionException {
        var procfs = new ProcFs();
        var propName = "/proc/sys/net/ipv4/ip_forward";
        Preconditions.isTrue(
                procfs.readInt(Paths.get(propName)) == 1,
                "Please enable forwarding in " + propName);
        propName = "/proc/sys/net/ipv4/conf/all/proxy_arp";
        Preconditions.isTrue(
                procfs.readInt(Paths.get(propName)) == 1, "Please enable ARP proxy in " + propName);
    }
}
