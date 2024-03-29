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

import static java.lang.System.exit;

import id.fireguard.Settings;
import id.fireguard.net.NetworkManager;
import id.fireguard.net.NetworkManagerBuilder;
import id.fireguard.vmm.VirtualMachineManager;
import id.fireguard.vmm.VirtualMachinesStore;
import id.xfunction.cli.CommandLineInterface;
import id.xfunction.cli.SmartArgs;
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

/**
 * @author lambdaprime intid@protonmail.com
 */
public class FireGuardApp {

    private static Optional<Path> configPath = Optional.empty();
    private static Map<String, Consumer<String>> handlers =
            Map.of(
                    "--config",
                    val -> {
                        configPath = Optional.of(Paths.get(val));
                    });
    public static CommandLineInterface cli = new CommandLineInterface();

    @SuppressWarnings("resource")
    private static void usage() throws IOException {
        try (Scanner scanner =
                new Scanner(FireGuardApp.class.getResource("/README-fireguard.md").openStream())
                        .useDelimiter("\n")) {
            while (scanner.hasNext()) cli.print(scanner.next());
        }
    }

    private static void setup(Path stage) {
        File f = stage.toFile();
        if (f.exists()) return;
        f.mkdirs();
    }

    public void run(List<String> positionalArgs, Optional<Path> configPath) throws Exception {
        var settings = Settings.load(configPath);
        setup(settings.getStage());

        var cmd = positionalArgs.remove(0);
        switch (cmd) {
            case "vm":
                {
                    var vmm = createVmm(settings);
                    vmm.addBeforeStartListener(createNm(settings)::onBeforeVmStart);
                    new VmCommand(vmm, cli).execute(positionalArgs);
                    break;
                }
            case "net":
                {
                    var nm = createNm(settings);
                    nm.addOnAfterAttachListener(createVmm(settings)::onAttach);
                    new NetCommand(nm, cli).execute(positionalArgs);
                    break;
                }
            default:
                usage();
        }
    }

    private NetworkManager createNm(Settings settings) {
        return new NetworkManagerBuilder(settings).create();
    }

    private VirtualMachineManager createVmm(Settings settings) {
        var pm = new VirtualMachinesStore(settings.getVmStore());
        return VirtualMachineManager.create(settings, pm);
    }

    public static void main(String[] args) throws Exception {
        List<String> positionalArgs = new LinkedList<>();
        new SmartArgs(handlers, positionalArgs::add).parse(args);

        if (positionalArgs.size() < 1) {
            usage();
            exit(1);
        }

        try {
            new FireGuardApp().run(positionalArgs, configPath);
        } catch (CommandIllegalArgumentException e) {
            usage();
            exit(1);
        }
    }
}
