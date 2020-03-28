/*
 * Copyright 2020 lambdaprime
 */

package id.fireguard.tests;

import java.util.LinkedList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import id.fireguard.CommandIllegalArgumentException;
import id.fireguard.NetCommand;

public class NetCommandTests {

    @Test
    public void test_execute_noargs() {
    	Assertions.assertThrows(CommandIllegalArgumentException.class,
    			() -> new NetCommand(null).execute(new LinkedList<>()));
    }

    @Test
    public void test_create_noargs() {
    	LinkedList<String> args = new LinkedList<>();
    	args.add("create");
		Assertions.assertThrows(CommandIllegalArgumentException.class,
    			() -> new NetCommand(null).execute(args));
    }

}
