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
    
    public void test_no_args() throws Exception {
        Assertions.assertEquals(XUtils.readResource("README.md"),
                new XExec(FIREGUARD_PATH).run().stdoutAsString());
    }
    
    public void test_vm_showAll_empty() throws Exception {
        Assertions.assertEquals("",
                run("vm showAll").run().stdoutAsString());
    }

    public void test_vm_create() throws Exception {
        var out = run("vm create").run().stdoutAsString();
        System.out.println(out);
        Assertions.assertTrue(new TemplateMatcher(XUtils.readResource(
                getClass(), "vm-create")).matches(out));
    }
    
    public void test_vm_showAll() throws Exception {
        run("vm create").run().stdout().forEach(System.out::println);
        run("vm create").run().stdout().forEach(System.out::println);
        var out = run("vm showAll").run().stdoutAsString();
        System.out.println(out);
        Assertions.assertTrue(new TemplateMatcher(XUtils.readResource(
                getClass(), "vm-showAll")).matches(out));
    }

    public void test_vm_start() throws Exception {
        var out1 = run("vm start vm-1").run().stdoutAsString();
        System.out.println(out1);
        var out2 = run("vm showAll").run().stdoutAsString();
        System.out.println(out2);
        Assertions.assertEquals("Starting VM with id vm-1...\n", out1);
        Assertions.assertTrue(new TemplateMatcher(XUtils.readResource(
                getClass(), "vm-start")).matches(out2));
    }

    public void test_net_create() throws Exception {
        var out = run("net create 10.1.2.0 255.255.255.0").run().stdoutAsString();
        System.out.println(out);
        Assertions.assertTrue(new TemplateMatcher(XUtils.readResource(
                getClass(), "net-create")).matches(out));
    }
    
    @Test
    public void test() throws Exception {
        test_no_args();
        test_vm_showAll_empty();
        test_vm_create();
        test_vm_showAll();
        test_vm_start();
        test_net_create();
    }
    
    private XExec run(String args) {
        return new XExec(FIREGUARD_PATH + " --config " + config.toString() + " " + args);
    }
}