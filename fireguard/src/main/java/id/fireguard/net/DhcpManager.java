package id.fireguard.net;

import java.net.InetAddress;
import java.util.List;
import java.util.function.Predicate;

import id.jnix.net.dhcpd.Dhcpd;
import id.jnix.net.dhcpd.DhcpdConfig;
import id.jnix.net.dhcpd.Host;
import id.jnix.net.dhcpd.SubnetSection;
import id.xfunction.function.Unchecked;

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
        var subnetSection = dhcpdConfig.getSubnet(network.getSubnet()).orElseGet(() -> {
            var s = new SubnetSection(network.getSubnet(), network.getNetmask());
            dhcpdConfig.addSubnet(s);
            return s;
        });
        var host = new Host(iface.getName(), iface.getMac().toString(), iface.getVmIp())
                .withRouters(List.of(iface.getHostIp()))
                .withDns(Unchecked.get(() -> List.of(InetAddress.getByName("8.8.4.4"),
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
        Unchecked.run(() -> new Dhcpd()
                .start(dhcpdConfig));
    }

    public void restart() {
        stop();
        start();
    }
}
