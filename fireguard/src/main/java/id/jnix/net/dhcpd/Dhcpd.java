/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.net.dhcpd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// Can't open /tmp/dhcpd105391253209477051: Permission denied
// apparmor/selinux
// sudo ln -s /etc/apparmor.d/usr.sbin.dhcpd /etc/apparmor.d/disable/
// sudo apparmor_parser -R /etc/apparmor.d/usr.sbin.dhcpd 
public class Dhcpd {

    private boolean withSudo;

    public Dhcpd withSudo() {
        this.withSudo = true;
        return this;
    }

    public Process start(DhcpdConfig conf) throws Exception {
        Path configFile = Paths.get(conf.getConfigLocation());
        Files.write(configFile, conf.toString().getBytes());
        return run(configFile);
    }

    private Process run(Path configFile) throws IOException {
        var cmd = new ArrayList<String>();
        if (withSudo)
            cmd.add("sudo");
        cmd.add("dhcpd");
        cmd.add("-cf");
        cmd.add(configFile.toString());
        var pb = new ProcessBuilder(cmd);
        return pb.start();
    }

}
