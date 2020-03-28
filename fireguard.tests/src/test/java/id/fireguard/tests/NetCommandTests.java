/*
 * Copyright 2020 lambdaprime
 */

package id.fireguard.tests;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.fireguard.CommandIllegalArgumentException;
import id.fireguard.NetCommand;
import id.fireguard.net.NetworkManager;
import id.fireguard.net.NetworkStore;
import id.xfunction.ObjectsStore;

public class NetCommandTests {

	private NetCommand nc;
	private NetworkManager nm;

	@BeforeEach
	void setup() {
		nm = NetworkManager.create(NetworkStore.create(
				ObjectsStore.create(new HashSet<>())));
		nc = new NetCommand(nm);
	}
	
    @Test
    public void test_execute_noargs() {
    	Assertions.assertThrows(CommandIllegalArgumentException.class,
    			() -> nc.execute(new LinkedList<>()));
    }

    @Test
    public void test_create_noargs() {
    	List<String> args = new LinkedList<>(List.of("create"));
		Assertions.assertThrows(CommandIllegalArgumentException.class,
    			() -> nc.execute(args));
    }

    @Test
    public void test_create_uniq_subnets() throws Exception {
    	List<String> args = List.of(
    			"create", "10.1.1.1", "255.255.0.0");
		nc.execute(new LinkedList<>(args));
		Assertions.assertThrows(RuntimeException.class,
				() -> nc.execute(new LinkedList<>(args)));
    }
}
