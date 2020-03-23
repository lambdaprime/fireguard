module id.fireguard {
    requires id.xfunction;
    requires com.google.gson;
    
    exports id.jnix.net.dhcpd;
    exports id.fireguard.vmm to id.fireguard.tests;
    opens id.fireguard.vmm.vmconfig to com.google.gson;
}