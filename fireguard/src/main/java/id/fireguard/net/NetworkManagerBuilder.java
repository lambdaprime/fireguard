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
package id.fireguard.net;

import id.fireguard.Settings;
import id.xfunction.ObjectStore;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class NetworkManagerBuilder {

    private Settings settings;

    public NetworkManagerBuilder(Settings settings) {
        this.settings = settings;
    }

    public NetworkManager create() {
        var networkStore = new NetworkStore(settings.getNetStore());
        var config = retrieveConfig();
        var dhcpManager = createDhcpManager();
        NetworkManager nm =
                new NetworkManager(
                        config,
                        networkStore,
                        new NetworkTransformer(),
                        new NetworkInstaller(dhcpManager, settings));
        return nm;
    }

    private DhcpManager createDhcpManager() {
        var store =
                new ObjectStore<DhcpManagerConfig>(
                        settings.getConfigsLocation().resolve("dhcpManagerConfig"));
        var config = store.load().orElse(new DhcpManagerConfig());
        config.addListener(cfg -> store.save(cfg));
        return new DhcpManager(config);
    }

    private NetworkManagerConfig retrieveConfig() {
        var store =
                new ObjectStore<NetworkManagerConfig>(
                        settings.getConfigsLocation().resolve("nmconfig"));
        var config = store.load().orElse(new NetworkManagerConfig());
        config.addListener(store::save);
        return config;
    }
}
