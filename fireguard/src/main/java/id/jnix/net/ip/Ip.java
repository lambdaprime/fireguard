/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.net.ip;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import id.jnix.CommandExecutionException;
import id.jnix.internal.Utils;
import id.xfunction.XExec;
import id.xfunction.XProcess;

public class Ip {

    public enum Status {
        up, down
    }

    public enum TunnelMode {
        tap
    }

    private Utils utils = new Utils();
    private boolean withSudo;

    public Ip withSudo() {
        this.withSudo = true;
        return this;
    }

    public List<Address> address() throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        utils.sudo(withSudo, cmd);
        cmd.add("ip");
        cmd.add("-br");
        cmd.add("address");
        XProcess proc = new XExec(cmd)
                .run();
        utils.verifyCode(proc);
        return proc.stdout()
                .map(l -> l.split("\\s+"))
                .map(a -> new Address(a[0]))
                .collect(Collectors.toList());
    }

    public void addressAdd(String ifaceName, InetAddress address) throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        utils.sudo(withSudo, cmd);
        cmd.add("ip");
        cmd.add("address");
        cmd.add("add");
        cmd.add(address.getHostAddress());
        cmd.add("dev");
        cmd.add(ifaceName);
        XProcess proc = new XExec(cmd)
                .run();
        utils.verifyCode(proc);
    }

    public void tunTapAdd(String ifaceName, TunnelMode mode) throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        utils.sudo(withSudo, cmd);
        cmd.add("ip");
        cmd.add("tuntap");
        cmd.add("add");
        cmd.add(ifaceName);
        cmd.add("mode");
        cmd.add(mode.toString());
        XProcess proc = new XExec(cmd)
                .run();
        utils.verifyCode(proc);
    }

    public void tunTapDel(String ifaceName, TunnelMode mode) throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        utils.sudo(withSudo, cmd);
        cmd.add("ip");
        cmd.add("tuntap");
        cmd.add("del");
        cmd.add(ifaceName);
        cmd.add("mode");
        cmd.add(mode.toString());
        XProcess proc = new XExec(cmd)
                .run();
        utils.verifyCode(proc);
    }

    public void linkSet(String ifaceName, Status status) throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        utils.sudo(withSudo, cmd);
        cmd.add("ip");
        cmd.add("link");
        cmd.add("set");
        cmd.add(ifaceName);
        cmd.add(status.toString());
        XProcess proc = new XExec(cmd)
                .run();
        utils.verifyCode(proc);
    }
}
