/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.app;

import static java.lang.System.exit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

import id.fireguard.Settings;
import id.fireguard.net.NetworkManagerBuilder;
import id.fireguard.vmm.VirtualMachineManager;
import id.fireguard.vmm.VirtualMachinesStore;
import id.xfunction.CommandLineInterface;
import id.xfunction.SmartArgs;

public class FireGuardApp {

    private static Optional<Path> configPath = Optional.empty();
    private static Map<String, Consumer<String>> handlers = Map.of(
        "--config", val -> { configPath = Optional.of(Paths.get(val)); }
    );
    private static CommandLineInterface cli = new CommandLineInterface();

    @SuppressWarnings("resource")
    private static void usage() throws IOException {
        try (Scanner scanner = new Scanner(FireGuardApp.class.getResource("/README.md").openStream())
                .useDelimiter("\n")) {
            while (scanner.hasNext())
                cli.print(scanner.next());
        }
    }

    private static void setup(Path stage) {
        File f = stage.toFile();
        if (f.exists()) return;
        f.mkdirs();
    }

    private void run(List<String> positionalArgs) throws Exception {
        var settings = Settings.load(configPath);
        setup(settings.getStage());

        var cmd = positionalArgs.remove(0);
        switch (cmd) {
        case "vm": {
            new VmCommand(createVmm(settings), cli).execute(positionalArgs);
            break;
        }
        case "net": {
            var nm = new NetworkManagerBuilder(settings).create();
            nm.addOnAfterAttachListener(createVmm(settings)::onAttach);
            new NetCommand(nm, cli).execute(positionalArgs); break;
        }
        default: usage();
        }
    }

    private VirtualMachineManager createVmm(Settings settings) {
        var pm = new VirtualMachinesStore(settings.getVmStore());
        return VirtualMachineManager.create(settings, pm);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            usage();
            exit(1);
        }

        List<String> positionalArgs = new LinkedList<>();
        new SmartArgs(handlers, positionalArgs::add)
            .parse(args);

        try {
            new FireGuardApp().run(positionalArgs);
        } catch (CommandIllegalArgumentException e) {
            usage();
            exit(1);
        }
    }
}
