/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;

import org.junit.jupiter.api.Test;

import id.jnix.ProcFs;

public class ProcFsTests {

    @Test
    public void test_happy() throws Exception {
        var path = Files.createTempFile("profs", "");
        Files.writeString(path, "3");
        var procfs = new ProcFs();
        assertEquals(3, procfs.readInt(path));
    }

}
