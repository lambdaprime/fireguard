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
package id.fireguard;

import id.xfunction.Preconditions;
import id.xfunction.lang.XRE;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class Settings {

    private static String userHome = System.getProperty("user.home");
    private Path stage, originVm, vmStore, netStore;
    private Path configsLocation;
    private String hostIface;

    public Path getStage() {
        return stage;
    }

    public Path getOriginVM() {
        return originVm;
    }

    public static Settings load(Optional<Path> configPath) throws Exception {
        return load(configPath.orElse(Paths.get(userHome, ".fireguard")));
    }

    private static Settings load(Path configFile) throws Exception {
        Properties defaultProps = loadProperties(configFile);
        var settings = new Settings();
        var fireguardHome = getFireguardHome(defaultProps);
        var store = initLocation(fireguardHome, "store");
        settings.vmStore = store.resolve("vm");
        settings.netStore = store.resolve("net");
        settings.originVm = Paths.get(defaultProps.getProperty("originVm"));
        settings.stage = initLocation(fireguardHome, "stage");
        settings.hostIface = defaultProps.getProperty("hostIface");
        settings.configsLocation = initLocation(fireguardHome, "configs");
        Preconditions.notNull(
                settings.hostIface, "Wrong config file. Property 'hostIface' is missing.");
        return settings;
    }

    private static Path initLocation(Path fireguardHome, String name) {
        var loc = fireguardHome.resolve(name);
        loc.toFile().mkdirs();
        return loc;
    }

    private static Path getFireguardHome(Properties props) {
        String home = props.getProperty("fireguardHome", userHome + "/fireguardHome");
        Preconditions.notNull(home, "Wrong config file. Property 'fireguardHome' is missing.");
        var path = Paths.get(home);
        path.toFile().mkdirs();
        return path;
    }

    private static Properties loadProperties(Path configFile) throws Exception {
        Properties defaultProps = new Properties();
        File config = configFile.toFile();
        if (!config.exists()) {
            throw new XRE(
                    "Config file %s not found. Run fireguard with no arguments to see the example"
                            + " of it.",
                    config);
        }
        FileInputStream in = new FileInputStream(config);
        defaultProps.load(in);
        in.close();
        return defaultProps;
    }

    public Path getVmStore() {
        return vmStore;
    }

    public Path getNetStore() {
        return netStore;
    }

    public Path getConfigsLocation() {
        return configsLocation;
    }

    public String getHostIface() {
        return hostIface;
    }
}
