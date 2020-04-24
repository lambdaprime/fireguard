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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import id.fireguard.Settings;

public class SettingsTests {

    @Test
    public void test_store_not_set() {
        Assertions.assertThrows(AssertionError.class,
                () -> Settings.load(Files.createTempFile("gggg", "")));
    }

    @Test
    public void test_store_created() throws Exception {
        Path config = Files.createTempFile("gggg", "");
        Path storeDir = Paths.get("/tmp", "store" + System.currentTimeMillis());
        Files.write(config, List.of(
                "store = " + storeDir,
                "originVm = ",
                "stage = ",
                "firecracker = ",
                "hostIface = "));
        Settings.load(config);
        File store = storeDir.toFile();
        Assertions.assertTrue(store.isDirectory());
        Assertions.assertTrue(storeDir.toFile().exists());
    }
}
