/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.tests;

import static id.xfunction.XUtils.readResource;
import static id.xfunction.XUtils.readResourceAsStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import id.jnix.Jq;
import id.xfunction.XProcess;
import id.xfunction.function.Unchecked;

public class JqTests {

    @Test
    public void test_extract() throws Exception {
        var proc = new Jq()
                .withFilter(".age")
                .withInput(readResourceAsStream(getClass(), "jq1"))
                .run();
        assertEquals("27", new XProcess(proc).stdoutAsString());
    }

    @Test
    public void test_replace() throws Exception {
        var proc = new Jq()
                .withFilter("select(.id == \"2\").name = \"test\"")
                .withInput(readResourceAsStream(getClass(), "jq2"))
                .run();
        assertEquals(readResource(getClass(), "jq2_out"), new XProcess(proc).stdoutAsString());
    }

    @Test
    public void test_replace_inplace() throws Exception {
        var file = Paths.get("/tmp/jq.test");
        Files.write(file, readResource(getClass(), "jq2").getBytes(), StandardOpenOption.CREATE);
        var proc = new Jq()
                .withFilter("select(.id == \"2\").name = \"test\"")
                .withFile(file)
                .withInplaceMode()
                .run();
        proc.onExit().thenRun(Unchecked.wrapRun(() -> {
            assertEquals(readResource(getClass(), "jq2_out"), Files.readAllLines(file).stream()
                    .collect(Collectors.joining("\n")));
        }));
    }
}
