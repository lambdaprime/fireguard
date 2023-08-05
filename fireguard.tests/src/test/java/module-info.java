/**
 * Copyright 2020 lambdaprime
 *
 * <p>Email: intid@protonmail.com Website: https://github.com/lambdaprime
 */
module id.fireguard.tests {
    opens id.fireguard.tests;
    opens id.fireguard.tests.net;
    opens id.fireguard.tests.vmm.vmconfig;
    opens id.jnix.tests;
    opens id.jnix.tests.net.dhcpd;
    opens id.jnix.tests.net.ip;
    opens id.jnix.tests.net.iptables;

    requires id.fireguard;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.params;
    requires id.xfunction;
}
