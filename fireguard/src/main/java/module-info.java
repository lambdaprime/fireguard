/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
module id.fireguard {
    requires id.xfunction;

    exports id.jnix;
    exports id.jnix.net.ip;
    exports id.jnix.net.dhcpd;
    exports id.jnix.net.iptables;
    
    exports id.fireguard.app;
    
    exports id.fireguard to id.fireguard.tests;
    exports id.fireguard.vmm to id.fireguard.tests;
    exports id.fireguard.vmm.vmconfig to id.fireguard.tests;
    exports id.fireguard.net to id.fireguard.tests;
    opens id.fireguard.vmm.vmconfig to com.google.gson;
}