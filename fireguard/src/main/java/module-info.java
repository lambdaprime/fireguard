module id.fireguard {
    requires id.xfunction;
    requires com.google.gson;
    
    exports id.jnix;
    exports id.jnix.net.dhcpd;
    exports id.jnix.net.iptables;
    exports id.fireguard to id.fireguard.tests;
    exports id.fireguard.vmm to id.fireguard.tests;
    exports id.fireguard.net to id.fireguard.tests;
    exports id.fireguard.net.generators to id.fireguard.tests;
    opens id.fireguard.vmm.vmconfig to com.google.gson;
}