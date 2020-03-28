package id.fireguard;

import static java.lang.System.out;

import java.util.List;

import id.fireguard.net.Network;
import id.fireguard.net.NetworkManager;

public class NetCommand implements Command {

    private NetworkManager nm;

    public NetCommand(NetworkManager nm) {
        this.nm = nm;
    }

    private void create(List<String> positionalArgs) throws CommandIllegalArgumentException {
    	if (positionalArgs.size() != 2) throw new CommandIllegalArgumentException();
    	String subnet = positionalArgs.get(0);
    	String netmask = positionalArgs.get(1);
        out.println("Creating new network...");
        Network net = nm.create(subnet, netmask);
        out.println(net);
    }

    @Override
    public void execute(List<String> positionalArgs) throws CommandIllegalArgumentException {
    	if (positionalArgs.size() == 0) throw new CommandIllegalArgumentException();
    	var cmd = positionalArgs.remove(0);
        switch (cmd) {
        case "create": create(positionalArgs); break;
        default: throw new CommandIllegalArgumentException();
        }
    }

}
