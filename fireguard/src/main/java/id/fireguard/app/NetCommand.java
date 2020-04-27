/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.app;

import static id.xfunction.XAsserts.assertTrue;

import java.net.InetAddress;
import java.util.List;

import id.fireguard.net.Network;
import id.fireguard.net.NetworkManager;
import id.xfunction.CommandLineInterface;
import id.xfunction.function.Unchecked;

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
        assertTrue(Unchecked.getBoolean(() ->InetAddress.getByName(subnet).getAddress()[3] == 0),
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
        case "create": create(positionalArgs); break;
        case "showAll": showAll(); break;
        case "attach": attach(positionalArgs); break;
        default: throw new CommandIllegalArgumentException();
        }
    }

}
