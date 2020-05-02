/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.tests;

import static id.xfunction.XUtils.readResource;

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
import id.xfunction.XProcess;
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

    class Result {
        String stdout;
        String stderr;
        XProcess proc;
        public Result(XProcess proc) {
            this.stdout = proc.stdoutAsString();
            this.stderr = proc.stderrAsString();
            this.proc = proc;
        }
    }
    
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
    public void test() throws Exception {
        test_no_args();
        test_vm_showAll_empty();
        test_vm_create();
        test_vm_showAll();
        test_vm_start();
        test_net_create();
        test_net_showAll();
        test_net_attach();
        test_vm_showAll_after_attach();
        test_vm_stop();
    }

    private void test_no_args() throws Exception {
        Assertions.assertEquals(readResource("README.md"),
                new XExec(FIREGUARD_PATH).run().stdoutAsString());
    }
    
    private void test_vm_showAll_empty() throws Exception {
        Assertions.assertEquals("",
                run("vm showAll").stdout);
    }

    private void test_vm_create() throws Exception {
        var out = run("vm create").stdout;
        Assertions.assertTrue(new TemplateMatcher(readResource(
                getClass(), "vm-create")).matches(out));
    }
    
    private void test_vm_showAll() throws Exception {
        run("vm create");
        run("vm create");
        var out = run("vm showAll").stdout;
        Assertions.assertTrue(new TemplateMatcher(readResource(
                getClass(), "vm-showAll")).matches(out));
    }

    private void test_vm_start() throws Exception {
        var out1 = run("vm start vm-1").stdout;
        var out2 = run("vm showAll").stdout;
        Assertions.assertEquals("Starting VM with id vm-1...\n", out1);
        Assertions.assertTrue(new TemplateMatcher(readResource(
                getClass(), "vm-start")).matches(out2));
    }

    private void test_net_create() throws Exception {
        var out = run("net create 10.1.2.0 255.255.255.0").stdout;
        Assertions.assertTrue(new TemplateMatcher(readResource(
                getClass(), "net-create")).matches(out));
    }

    private void test_net_showAll() throws Exception {
        var out = run("net showAll").stdout;
        Assertions.assertTrue(new TemplateMatcher(readResource(
                getClass(), "net-showAll")).matches(out));
    }
    
    private void test_net_attach() {
        var out = run("net attach vm-2 net-1").stdout;
        Assertions.assertTrue(new TemplateMatcher(readResource(
                getClass(), "net-attach")).matches(out));
    }

    private void test_vm_showAll_after_attach() {
        var out = run("vm showAll").stdout;
        Assertions.assertTrue(new TemplateMatcher(readResource(
                getClass(), "vm-showAll-after-attach")).matches(out));
    }

    private void test_vm_stop() throws Exception {
        var out1 = run("vm stop vm-1").stdout;
        var out2 = run("vm showAll").stdout;
        Assertions.assertEquals("Stopping VM with id vm-1...\n", out1);
        Assertions.assertTrue(new TemplateMatcher(readResource(
                getClass(), "vm-showAll-after-stop")).matches(out2));
    }

    private Result run(String args) {
        var proc = new XExec(FIREGUARD_PATH + " --config " + config.toString() + " " + args)
                .run();
        var code = proc.getCode();
        Result res = new Result(proc);
        System.out.println(res.stdout);
        System.err.println(res.stderr);
        Assertions.assertEquals(0, code);
        return res;
    }
    
}
