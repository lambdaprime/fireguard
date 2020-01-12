package id.fireguard;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

import id.fireguard.vmm.VirtualMachinesStore;
import id.fireguard.vmm.VirtualMachine;
import id.fireguard.vmm.VirtualMachineManager;

public class FireGuardApp {

    private VirtualMachineManager vmm;

    public FireGuardApp(VirtualMachineManager vmm) {
        this.vmm = vmm;
    }

    @SuppressWarnings("resource")
    private static void usage() throws IOException {
        try (Scanner scanner = new Scanner(FireGuardApp.class.getResource("/README.md").openStream())
                .useDelimiter("\n")) {
            while (scanner.hasNext())
                out.println(scanner.next());
        }
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

    private static void setup(Path stage) {
        File f = stage.toFile();
        if (f.exists()) return;
        f.mkdirs();
    }

    private void create(String jqExpr) {
        out.println("Creating new VM...");
        VirtualMachine vm = vmm.create(jqExpr);
        out.println(vm);
    }

    private void update(String vmId, String jqExpr) {
        out.println("Updating VM with id " + vmId);
        var vm = vmm.update(vmId, jqExpr);
        out.println(vm);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            usage();
            exit(1);
        }
        var settings = Settings.load();
        var pm = VirtualMachinesStore.load(settings.getStore());
        setup(settings.getStage());

        var dbcm = VirtualMachineManager.create(settings, pm);
        var app = new FireGuardApp(dbcm);
        var cmd = args[0];
        switch (cmd) {
        case "create": app.create(args[1]); break;
        case "showAll": app.showAll(); break;
        case "startAll": app.startAll(); break;
        case "stopAll": app.stopAll(); break;
        case "restart": app.restart(args[1]); break;
        case "update": app.update(args[1], args[2]); break;
        case "start": app.start(args[1]); break;
        case "stop": app.stop(args[1]); break;
        default: usage();
        }

    }
}
