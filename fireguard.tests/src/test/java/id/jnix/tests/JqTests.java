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
package id.jnix.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import id.jnix.Jq;
import id.xfunction.ResourceUtils;
import id.xfunction.function.Unchecked;
import id.xfunction.lang.XProcess;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class JqTests {

    private static final ResourceUtils utils = new ResourceUtils();

    @Test
    public void test_extract() throws Exception {
        var proc =
                new Jq()
                        .withFilter(".age")
                        .withInput(utils.readResourceAsStream(getClass(), "jq1"))
                        .run();
        assertEquals("27", new XProcess(proc).stdout());
    }

    @Test
    public void test_replace() throws Exception {
        var proc =
                new Jq()
                        .withFilter("select(.id == \"2\").name = \"test\"")
                        .withInput(utils.readResourceAsStream(getClass(), "jq2"))
                        .run();
        assertEquals(utils.readResource(getClass(), "jq2_out"), new XProcess(proc).stdout());
    }

    @Test
    public void test_replace_inplace() throws Exception {
        var file = Paths.get("/tmp/jq.test");
        Files.write(
                file, utils.readResource(getClass(), "jq2").getBytes(), StandardOpenOption.CREATE);
        var proc =
                new Jq()
                        .withFilter("select(.id == \"2\").name = \"test\"")
                        .withFile(file)
                        .withInplaceMode()
                        .run();
        proc.onExit()
                .thenRun(
                        Unchecked.wrapRun(
                                () -> {
                                    assertEquals(
                                            utils.readResource(getClass(), "jq2_out"),
                                            Files.readAllLines(file).stream()
                                                    .collect(Collectors.joining("\n")));
                                }));
    }
}
