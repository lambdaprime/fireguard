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
package id.fireguard.tests;

import id.fireguard.Settings;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SettingsTests {

    @Test
    public void test_fireguardHome_created() throws Exception {
        Path config = Files.createTempFile("gggg", "");
        Path fireguardHome = Paths.get("/tmp", "store" + System.currentTimeMillis());
        Files.write(
                config,
                List.of(
                        "fireguardHome = " + fireguardHome,
                        "originVm = ",
                        "firecracker = ",
                        "hostIface = "));
        Settings.load(Optional.of(config));
        File store = fireguardHome.toFile();
        Assertions.assertTrue(store.isDirectory());
        Assertions.assertTrue(fireguardHome.toFile().exists());
    }
}
