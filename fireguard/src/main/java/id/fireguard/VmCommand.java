package id.fireguard;

import static java.lang.System.out;

import java.util.LinkedList;
import java.util.Optional;

import id.fireguard.vmm.VirtualMachine;
import id.fireguard.vmm.VirtualMachineManager;

public class VmCommand implements Command {

    private VirtualMachineManager vmm;

    public VmCommand(VirtualMachineManager vmm) {
        this.vmm = vmm;
    }

    private void startAll() {
        vmm.findAll()
            .stream()
            .map(VirtualMachine::getId)
            .forEach(this::start);
    }

    private void stopAll() {
        vmm.findAll()
            .stream()
            .map(VirtualMachine::getId)
            .forEach(this::stop);
    }

    private void stop(String vmId) {
        out.format("Stopping VM with id %s...\n", vmId);
        vmm.stop(vmId);
    }

    private void showAll() {
        vmm.findAll().forEach(out::println);
    }

    private void start(String vmId) {
        out.format("Starting VM with id %s...\n", vmId);
        vmm.start(vmId);
    }

    private void restart(String vmId) {
        stop(vmId);
        start(vmId);
    }

    private void create(Optional<String> jqExpr) {
        out.println("Creating new VM...");
        VirtualMachine vm = vmm.create(jqExpr);
        out.println(vm);
    }

    private void update(String vmId, String jqExpr) {
        out.println("Updating VM with id " + vmId);
        var vm = vmm.update(vmId, jqExpr);
        out.println(vm);
    }

    @Override
    public void execute(LinkedList<String> positionalArgs) throws CommandIllegalArgumentException {
    	var cmd = positionalArgs.removeFirst();
        switch (cmd) {
        case "create": create(positionalArgs.stream().findAny()); break;
        case "showAll": showAll(); break;
        case "startAll": startAll(); break;
        case "stopAll": stopAll(); break;
        case "restart": restart(positionalArgs.getFirst()); break;
        case "update": update(positionalArgs.get(0), positionalArgs.get(1)); break;
        case "start": start(positionalArgs.getFirst()); break;
        case "stop": stop(positionalArgs.getFirst()); break;
        default: throw new CommandIllegalArgumentException();
        }
    }

}
