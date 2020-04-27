/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.tests;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import id.fireguard.Settings;

public class SettingsTests {

    @Test
    public void test_fireguardHome_created() throws Exception {
        Path config = Files.createTempFile("gggg", "");
        Path fireguardHome = Paths.get("/tmp", "store" + System.currentTimeMillis());
        Files.write(config, List.of(
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
