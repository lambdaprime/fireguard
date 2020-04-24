/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.app;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import id.fireguard.Settings;
import id.fireguard.net.NetworkManagerBuilder;
import id.fireguard.vmm.VirtualMachineManager;
import id.fireguard.vmm.VirtualMachinesStore;
import id.xfunction.SmartArgs;

public class FireGuardApp {

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

    private void run(List<String> positionalArgs) throws Exception {
        var settings = Settings.load();
        setup(settings.getStage());

        var cmd = positionalArgs.remove(0);
        switch (cmd) {
        case "vm": {
            new VmCommand(createVmm(settings)).execute(positionalArgs);
            break;
        }
        case "net": {
            var nm = new NetworkManagerBuilder(settings).create();
            nm.onAttach(createVmm(settings)::onAttach);
            new NetCommand(nm).execute(positionalArgs); break;
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
        new SmartArgs(Map.of(), positionalArgs::add)
            .parse(args);

        try {
            new FireGuardApp().run(positionalArgs);
        } catch (CommandIllegalArgumentException e) {
            usage();
            exit(1);
        }
    }
}
