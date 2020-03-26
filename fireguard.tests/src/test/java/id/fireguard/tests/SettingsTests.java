/*
 * Copyright 2020 lambdaprime
 */

package id.fireguard.tests;

import java.nio.file.Files;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import id.fireguard.Settings;

public class SettingsTests {

    @Test
    public void test_store_not_set() {
    	Assertions.assertThrows(AssertionError.class,
    			() -> Settings.load(Files.createTempFile("gggg", "")));
    }

}
