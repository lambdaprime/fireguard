/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.net;

import id.fireguard.Settings;
import id.xfunction.ObjectStore;

public class NetworkManagerBuilder {

    private Settings settings;

    public NetworkManagerBuilder(Settings settings) {
        this.settings = settings;
    }

    public NetworkManager create() {
        var networkStore = new NetworkStore(settings.getNetStore());
        var config = retrieveConfig();
        var dhcpManager = createDhcpManager();
        NetworkManager nm = new NetworkManager(
                config,
                networkStore,
                new NetworkTransformer(),
                new NetworkInstaller(dhcpManager, settings)
                );
        return nm;
    }

    private DhcpManager createDhcpManager() {
        var store = new ObjectStore<DhcpManagerConfig>(
                settings.getConfigsLocation().resolve("dhcpManagerConfig"));
        var config = store.load()
                .orElse(new DhcpManagerConfig());
        config.addListener(cfg -> 
        store.save(cfg));
        return new DhcpManager(config);
    }

    private NetworkManagerConfig retrieveConfig() {
        var store = new ObjectStore<NetworkManagerConfig>(
                settings.getConfigsLocation().resolve("nmconfig"));
        var config = store.load()
                .orElse(new NetworkManagerConfig());
        config.addListener(store::save);
        return config;
    }

}
