package id.fireguard;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import id.fireguard.vmm.VirtualMachineManager;
import id.fireguard.vmm.VirtualMachinesStore;
import id.xfunction.SmartArgs;

public class FireGuardApp {

    private static LinkedList<String> positionalArgs = new LinkedList<>();

    @SuppressWarnings("resource")
    private static void usage() throws IOException {
        try (Scanner scanner = new Scanner(FireGuardApp.class.getResource("/README.md").openStream())
                .useDelimiter("\n")) {
            while (scanner.hasNext())
                out.println(scanner.next());
        }
    }

    private static void setup(Path stage) {
        File f = stage.toFile();
        if (f.exists()) return;
        f.mkdirs();
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            usage();
            exit(1);
        }

        new SmartArgs(Map.of(), positionalArgs::add)
        	.parse(args);

        var settings = Settings.load();
        var pm = VirtualMachinesStore.load(settings.getStore());
        setup(settings.getStage());

        var vmm = VirtualMachineManager.create(settings, pm);
        var cmd = positionalArgs.removeFirst();
        switch (cmd) {
        case "vm": new VmCommand(vmm).execute(positionalArgs); break;
        default: usage();
        }

    }
}
