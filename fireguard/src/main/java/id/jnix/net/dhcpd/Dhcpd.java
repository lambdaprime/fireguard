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

import id.jnix.CommandExecutionException;
import id.jnix.CommandHasSudo;
import id.jnix.internal.Utils;
import id.xfunction.XExec;
import id.xfunction.XProcess;

/**
 * Requires sudo access to /etc/init.d/isc-dhcp-server
 * and write permission to /etc/dhcp/dhcpd.conf
 */
public class Dhcpd extends CommandHasSudo<Dhcpd> {

    private static final Path configFile = Paths.get("/etc/dhcp/dhcpd.conf");
    private Utils utils = new Utils();
    
    public Dhcpd() {
        withSudo();
    }
    
    public Process start(DhcpdConfig conf) throws CommandExecutionException, IOException {
        Files.write(configFile, conf.toString().getBytes());
        return run("start").process();
    }

    public void stop() throws CommandExecutionException {
        run("stop");
    }

    public boolean isRunning() throws CommandExecutionException {
        return run("status").getCode() == 0;
    }

    private XProcess run(String command) throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        sudo(cmd);
        cmd.add("/etc/init.d/isc-dhcp-server");
        cmd.add(command);
        XProcess proc = new XExec(cmd)
                .run();
        utils.verifyCode(proc);
        return proc;
    }

    @Override
    protected Dhcpd self() {
        return this;
    }
}
