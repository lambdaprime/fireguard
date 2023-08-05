/*
 * Copyright 2020 fireguard project
 * 
 * Website: https://github.com/lambdaprime/fireguard
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.jnix.net.ip;

import id.jnix.CommandExecutionException;
import id.jnix.CommandHasSudo;
import id.jnix.internal.Utils;
import id.xfunction.lang.XExec;
import id.xfunction.lang.XProcess;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class Ip extends CommandHasSudo<Ip> {

    public enum Status {
        up,
        down
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
        XProcess proc = new XExec(cmd).start();
        utils.verifyCode(proc);
        return proc.stdoutAsStream()
                .map(l -> l.split("\\s+"))
                .map(a -> new Address(a[0]))
                .collect(Collectors.toList());
    }

    public void addressAdd(String ifaceName, InetAddress address) throws CommandExecutionException {
        addressAdd(ifaceName, address, 32);
    }

    public void addressAdd(String ifaceName, InetAddress address, int mask)
            throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        sudo(cmd);
        cmd.add("ip");
        cmd.add("address");
        cmd.add("add");
        cmd.add(address.getHostAddress() + "/" + mask);
        cmd.add("dev");
        cmd.add(ifaceName);
        XProcess proc = new XExec(cmd).start();
        utils.verifyCode(proc);
    }

    public List<Route> route() throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        sudo(cmd);
        cmd.add("ip");
        cmd.add("-br");
        cmd.add("route");
        XProcess proc = new XExec(cmd).start();
        utils.verifyCode(proc);
        return proc.stdoutAsStream()
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
        XProcess proc = new XExec(cmd).start();
        utils.verifyCode(proc);
    }

    public void routeDel(Route route) throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        sudo(cmd);
        cmd.add("ip");
        cmd.add("route");
        cmd.add("del");
        cmd.addAll(routeCommand(route));
        XProcess proc = new XExec(cmd).start();
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
        XProcess proc = new XExec(cmd).start();
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
        XProcess proc = new XExec(cmd).start();
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
        XProcess proc = new XExec(cmd).start();
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
