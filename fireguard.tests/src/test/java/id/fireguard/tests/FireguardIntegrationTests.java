/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.xfunction.TemplateMatcher;
import id.xfunction.XExec;
import id.xfunction.XUtils;

public class FireguardIntegrationTests {

    private static final String ORIGIN_PATH = Paths.get("")
            .toAbsolutePath()
            .getParent()
            .resolve("origin")
            .toString();
    private static final String FIREGUARD_PATH = Paths.get("")
            .toAbsolutePath()
            .resolve("build/fireguard/fireguard")
            .toString();
    private Path fireguardHome;
    private Path config;
    
    @BeforeEach
    void setup() throws IOException {
        config = Files.createTempFile("gggg", "");
        fireguardHome = Paths.get("/tmp", "store" + System.currentTimeMillis());
        Files.write(config, List.of(
                "fireguardHome = " + fireguardHome,
                "originVm = " + ORIGIN_PATH,
                "hostIface = test"));
    }
    
    @AfterEach
    void cleanup() throws IOException {
        Files.delete(config);
        XUtils.deleteDir(fireguardHome);
    }
    
    @Test
    public void test_no_args() throws Exception {
        Assertions.assertEquals(XUtils.readResource("README.md"),
                new XExec(FIREGUARD_PATH).run().stdoutAsString());
    }
    
    @Test
    public void test_vm_showAll() throws Exception {
        Assertions.assertEquals("",
                run("vm showAll").run().stdoutAsString());
    }

    @Test
    public void test_vm_create() throws Exception {
        var out = run("vm create").run().stdoutAsString();
        System.out.println(out);
        Assertions.assertTrue(new TemplateMatcher(XUtils.readResource(
                getClass(), "vm-create")).matches(out));
    }
    
    private XExec run(String args) {
        return new XExec(FIREGUARD_PATH + " --config " + config.toString() + " " + args);
    }
}
