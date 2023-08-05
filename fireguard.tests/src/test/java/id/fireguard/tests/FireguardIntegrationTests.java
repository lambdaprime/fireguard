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

import id.xfunction.ResourceUtils;
import id.xfunction.lang.XExec;
import id.xfunction.lang.XProcess;
import id.xfunction.nio.file.XFiles;
import id.xfunction.text.WildcardMatcher;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FireguardIntegrationTests {

    private static final ResourceUtils utils = new ResourceUtils();
    private static final String ORIGIN_PATH =
            Paths.get("")
                    .toAbsolutePath()
                    .getParent()
                    .resolve("origin/alpinelinux-3.8-kernel4.14")
                    .toString();
    private static final String FIREGUARD_PATH =
            Paths.get("").toAbsolutePath().resolve("build/fireguard/fireguard").toString();
    private Path fireguardHome;
    private Path config;

    class Result {
        String stdout;
        String stderr;
        XProcess proc;

        public Result(XProcess proc) {
            this.stdout = proc.stdout();
            this.stderr = proc.stderr();
            this.proc = proc;
        }
    }

    @BeforeEach
    void setup() throws IOException {
        fireguardHome = Paths.get("/tmp", "store" + System.currentTimeMillis());
        Files.createDirectory(fireguardHome);
        config = fireguardHome.resolve(".fireguard");
        Files.write(
                config,
                List.of(
                        "fireguardHome = " + fireguardHome,
                        "originVm = " + ORIGIN_PATH,
                        "hostIface = test"),
                StandardOpenOption.CREATE_NEW);
    }

    @AfterEach
    void cleanup() throws IOException {
        XFiles.deleteRecursively(fireguardHome);
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
        test_vm_startAll();
        test_vm_showAll_after_startAll();
        test_vm_stopAll_after_startAll();
        test_vm_showAll_after_stopAll();
    }

    private void test_no_args() throws Exception {
        Assertions.assertEquals(
                utils.readResource("README-fireguard.md"),
                new XExec(FIREGUARD_PATH).start().stdout());
    }

    private void test_vm_showAll_empty() throws Exception {
        Assertions.assertEquals("", run("vm showAll").stdout);
    }

    private void test_vm_create() throws Exception {
        var out = run("vm create").stdout;
        Assertions.assertTrue(
                new WildcardMatcher(utils.readResource(getClass(), "vm-create")).matches(out));
    }

    private void test_vm_showAll() throws Exception {
        run("vm create");
        run("vm create");
        var out = run("vm showAll").stdout;
        Assertions.assertTrue(
                new WildcardMatcher(utils.readResource(getClass(), "vm-showAll")).matches(out));
    }

    private void test_vm_start() throws Exception {
        var out1 = run("vm start vm-1").stdout;
        var out2 = run("vm showAll").stdout;
        Assertions.assertEquals("Starting VM with id vm-1...\n", out1);
        Assertions.assertTrue(
                new WildcardMatcher(utils.readResource(getClass(), "vm-start")).matches(out2));
    }

    private void test_net_create() throws Exception {
        var out = run("net create 10.1.2.0 255.255.255.0").stdout;
        Assertions.assertTrue(
                new WildcardMatcher(utils.readResource(getClass(), "net-create")).matches(out));
    }

    private void test_net_showAll() throws Exception {
        var out = run("net showAll").stdout;
        Assertions.assertTrue(
                new WildcardMatcher(utils.readResource(getClass(), "net-showAll")).matches(out));
    }

    private void test_net_attach() {
        var out = run("net attach vm-2 net-1").stdout;
        Assertions.assertTrue(
                new WildcardMatcher(utils.readResource(getClass(), "net-attach")).matches(out));
    }

    private void test_vm_showAll_after_attach() {
        var out = run("vm showAll").stdout;
        Assertions.assertTrue(
                new WildcardMatcher(utils.readResource(getClass(), "vm-showAll-after-attach"))
                        .matches(out));
    }

    private void test_vm_stop() throws Exception {
        var out1 = run("vm stop vm-1").stdout;
        var out2 = run("vm showAll").stdout;
        Assertions.assertEquals("Stopping VM with id vm-1...\n", out1);
        Assertions.assertTrue(
                new WildcardMatcher(utils.readResource(getClass(), "vm-showAll-after-stop"))
                        .matches(out2));
    }

    private void test_vm_startAll() throws Exception {
        var proc = run("vm startAll");
        Assertions.assertTrue(
                new WildcardMatcher(utils.readResource(getClass(), "vm-startAll"))
                        .matches(proc.stdout));
    }

    private void test_vm_showAll_after_startAll() throws Exception {
        var proc = run("vm showAll");
        Assertions.assertTrue(
                new WildcardMatcher(utils.readResource(getClass(), "vm-showAll-after-startAll"))
                        .matches(proc.stdout));
    }

    private void test_vm_stopAll_after_startAll() throws Exception {
        var proc = run("vm stopAll");
        Assertions.assertTrue(
                new WildcardMatcher(utils.readResource(getClass(), "vm-stopAll-after-startAll"))
                        .matches(proc.stdout));
    }

    private void test_vm_showAll_after_stopAll() throws Exception {
        var proc = run("vm showAll");
        Assertions.assertTrue(
                new WildcardMatcher(utils.readResource(getClass(), "vm-showAll-after-stopAll"))
                        .matches(proc.stdout));
    }

    private Result run(String args) {
        var proc =
                new XExec(FIREGUARD_PATH + " --config " + config.toString() + " " + args).start();
        var code = proc.await();
        Result res = new Result(proc);
        System.out.println(res.stdout);
        System.err.println(res.stderr);
        Assertions.assertEquals(0, code);
        return res;
    }
}
