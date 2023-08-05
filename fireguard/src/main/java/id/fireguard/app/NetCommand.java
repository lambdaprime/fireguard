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
package id.fireguard.app;

import id.fireguard.net.Network;
import id.fireguard.net.NetworkManager;
import id.xfunction.Preconditions;
import id.xfunction.cli.CommandLineInterface;
import id.xfunction.function.Unchecked;
import java.net.InetAddress;
import java.util.List;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class NetCommand implements Command {

    private NetworkManager nm;
    private CommandLineInterface cli;

    public NetCommand(NetworkManager nm, CommandLineInterface cli) {
        this.nm = nm;
        this.cli = cli;
    }

    private void create(List<String> positionalArgs) throws CommandIllegalArgumentException {
        if (positionalArgs.size() != 2) throw new CommandIllegalArgumentException();
        String subnet = positionalArgs.get(0);
        String netmask = positionalArgs.get(1);
        Preconditions.isTrue(
                Unchecked.getBoolean(() -> InetAddress.getByName(subnet).getAddress()[3] == 0),
                "Wrong subnet format");
        cli.print("Creating new network...");
        Network net = nm.create(subnet, netmask);
        cli.print(net);
    }

    private void showAll() {
        nm.findAll().forEach(cli::print);
    }

    private void attach(List<String> positionalArgs) throws CommandIllegalArgumentException {
        if (positionalArgs.size() != 2) throw new CommandIllegalArgumentException();
        String vmId = positionalArgs.get(0);
        String netId = positionalArgs.get(1);
        cli.print("Attaching %s to %s network...\n", vmId, netId);
        nm.attach(vmId, netId);
    }

    @Override
    public void execute(List<String> positionalArgs) throws CommandIllegalArgumentException {
        if (positionalArgs.size() == 0) throw new CommandIllegalArgumentException();
        var cmd = positionalArgs.remove(0);
        switch (cmd) {
            case "create":
                create(positionalArgs);
                break;
            case "showAll":
                showAll();
                break;
            case "attach":
                attach(positionalArgs);
                break;
            default:
                throw new CommandIllegalArgumentException();
        }
    }
}
