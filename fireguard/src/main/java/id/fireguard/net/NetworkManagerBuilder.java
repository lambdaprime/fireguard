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
        NetworkManager nm = new NetworkManager(config, networkStore, new NetworkTransformer());
        return nm;
    }

    private NetworkManagerConfig retrieveConfig() {
        var store = new ObjectStore<NetworkManagerConfig>(settings.getNetworkManagerConfig());
        var config = store.load()
                .orElse(new NetworkManagerConfig());
        config.addListener(store::save);
        return config;
    }

}
