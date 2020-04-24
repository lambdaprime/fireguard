/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard;

import static id.xfunction.XUtils.throwRuntime;
import static id.xfunction.XAsserts.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Settings {

    private static String userHome = System.getProperty("user.home");
    private Path stage, originVm, vmStore, netStore, firecracker;
    private Path configsLocation;
    private String hostIface;

    public Path getStage() {
        return stage;
    }

    public Path getOriginVM() {
        return originVm;
    }

    public static Settings load() throws Exception {
        return load(Paths.get(userHome , ".fireguard"));
    }

    public static Settings load(Path configFile) throws Exception {
        Properties defaultProps = loadProperties(configFile);
        var settings = new Settings();
        var fireguardHome = getFireguardHome(defaultProps);
        var store = initLocation(fireguardHome, "store");
        settings.vmStore = store.resolve("vm");
        settings.netStore = store.resolve("net");
        settings.originVm = Paths.get(defaultProps.getProperty("originVm"));
        settings.stage = initLocation(fireguardHome, "stage");
        settings.firecracker = Paths.get(defaultProps.getProperty("firecracker"));
        settings.hostIface = defaultProps.getProperty("hostIface");
        settings.configsLocation = initLocation(fireguardHome, "configs");
        assertNotNull(settings.hostIface, "Wrong config file. Property 'hostIface' is missing.");
        return settings;
    }

    private static Path initLocation(Path fireguardHome, String name) {
        var loc = fireguardHome.resolve(name);
        loc.toFile().mkdirs();
        return loc;
    }

    private static Path getFireguardHome(Properties props) {
        String home = props.getProperty("fireguardHome", userHome + "/fireguardHome");
        assertNotNull(home, "Wrong config file. Property 'fireguardHome' is missing.");
        var path = Paths.get(home);
        path.toFile().mkdirs();
        return path;
    }

    private static Properties loadProperties(Path configFile) throws Exception {
        Properties defaultProps = new Properties();
        File config = configFile.toFile();
        if (!config.exists()) {
            throwRuntime("Config file %s not found. Run fireguard with no arguments to see the example of it.",
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

    public Path getFirecracker() {
        return firecracker;
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
