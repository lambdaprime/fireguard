package id.fireguard;

import static id.xfunction.XUtils.error;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Settings {

    private Path stage, originVm, vmStore, firecracker;

    public Path getStage() {
        return stage;
    }

    public Path getOriginVM() {
        return originVm;
    }

    public static Settings load() throws Exception {
        Properties defaultProps = loadProperties();
        var settings = new Settings();
        settings.vmStore = Paths.get(defaultProps.getProperty("store"), "vm");
        settings.originVm = Paths.get(defaultProps.getProperty("originVm"));
        settings.stage = Paths.get(defaultProps.getProperty("stage"));
        settings.firecracker = Paths.get(defaultProps.getProperty("firecracker"));
        return settings;
    }

    private static Properties loadProperties() throws Exception {
        Properties defaultProps = new Properties();
        File config = Paths.get(System.getProperty("user.home"), ".fireguard").toFile();
        if (!config.exists()) {
            error(String.format("Config file %s not found", config));
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
}
