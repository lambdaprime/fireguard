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

import id.jnix.ProcFs;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;

public class ProcFsTests {

    @Test
    public void test_happy() throws Exception {
        var path = Files.createTempFile("profs", "");
        Files.writeString(path, "3");
        var procfs = new ProcFs();
        assertEquals(3, procfs.readInt(path));
    }
}
