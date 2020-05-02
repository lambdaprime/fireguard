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
import java.util.Optional;
import java.util.stream.Collectors;

import id.jnix.CommandExecutionException;
import id.jnix.CommandHasSudo;
import id.jnix.internal.Utils;
import id.xfunction.XExec;
import id.xfunction.XProcess;

public class Ip extends CommandHasSudo<Ip> {

    public enum Status {
        up, down
    }

    public enum TunnelMode {
        tap
    }

    private Utils utils = new Utils();

    public List<Address> address() throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        sudo(cmd);
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
        addressAdd(ifaceName, address, 32);
    }
    
    public void addressAdd(String ifaceName, InetAddress address, int mask) throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        sudo(cmd);
        cmd.add("ip");
        cmd.add("address");
        cmd.add("add");
        cmd.add(address.getHostAddress() + "/" + mask);
        cmd.add("dev");
        cmd.add(ifaceName);
        XProcess proc = new XExec(cmd)
                .run();
        utils.verifyCode(proc);
    }

    public List<Route> route() throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        sudo(cmd);
        cmd.add("ip");
        cmd.add("-br");
        cmd.add("route");
        XProcess proc = new XExec(cmd)
                .run();
        utils.verifyCode(proc);
        return proc.stdout()
                .map(Route::parse)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void routeAdd(Route route) throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        sudo(cmd);
        cmd.add("ip");
        cmd.add("route");
        cmd.add("add");
        cmd.addAll(routeCommand(route));
        XProcess proc = new XExec(cmd)
                .run();
        utils.verifyCode(proc);
    }

    public void routeDel(Route route) throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        sudo(cmd);
        cmd.add("ip");
        cmd.add("route");
        cmd.add("del");
        cmd.addAll(routeCommand(route));
        XProcess proc = new XExec(cmd)
                .run();
        utils.verifyCode(proc);
    }

    public void tunTapAdd(String ifaceName, TunnelMode mode) throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        sudo(cmd);
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
        sudo(cmd);
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
        sudo(cmd);
        cmd.add("ip");
        cmd.add("link");
        cmd.add("set");
        cmd.add(ifaceName);
        cmd.add(status.toString());
        XProcess proc = new XExec(cmd)
                .run();
        utils.verifyCode(proc);
    }

    private List<String> routeCommand(Route route) {
        var cmd = new ArrayList<String>();
        cmd.add(route.getDestination() + "/" + route.getMask());
        cmd.add("dev");
        cmd.add(route.getDevice());
        return cmd;
    }

    @Override
    protected Ip self() {
        return this;
    }

}
