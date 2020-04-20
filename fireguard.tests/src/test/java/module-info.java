/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
module id.fireguard.tests {
    exports id.fireguard.tests;
    opens id.jnix.tests;
    opens id.jnix.tests.net.dhcpd;

    requires id.fireguard;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.params;
    requires id.xfunction;
}