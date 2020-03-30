package id.fireguard;

import static id.xfunction.XUtils.error;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import id.xfunction.XAsserts;

public class Settings {

	private Path stage, originVm, vmStore, netStore, firecracker;
	private Path networkManagerConfig;

	public Path getStage() {
        return stage;
    }

    public Path getOriginVM() {
        return originVm;
    }

    public static Settings load() throws Exception {
    	return load(Paths.get(System.getProperty("user.home"), ".fireguard"));
    }
    
    public static Settings load(Path configFile) throws Exception {
        Properties defaultProps = loadProperties(configFile);
        var settings = new Settings();
        var store = getStore(defaultProps.getProperty("store"));
        settings.vmStore = store.resolve("vm");
        settings.netStore = store.resolve("net");
        settings.networkManagerConfig = store.resolve("nmconfig");
        settings.originVm = Paths.get(defaultProps.getProperty("originVm"));
        settings.stage = Paths.get(defaultProps.getProperty("stage"));
        settings.firecracker = Paths.get(defaultProps.getProperty("firecracker"));
        return settings;
    }

    private static Path getStore(String store) {
    	XAsserts.assertNotNull(store, "Wrong config file. Property 'store' is missing.");
		var path = Paths.get(store);
		path.toFile().mkdirs();
		return path;
	}

	private static Properties loadProperties(Path configFile) throws Exception {
        Properties defaultProps = new Properties();
        File config = configFile.toFile();
        if (!config.exists()) {
            error(String.format("Config file %s not found. Run fireguard with no arguments to see the example of it.",
            		config));
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

	public Path getNetworkManagerConfig() {
		return networkManagerConfig;
	}
}
