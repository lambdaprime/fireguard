/*
 * Copyright 2020 lambdaprime
 */

package id.fireguard.tests;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import id.fireguard.vmm.VirtualMachineEntity;
import id.fireguard.vmm.VirtualMachinesStore;

public class VirtualMachinesStoreTest {

    @Test
    public void test_happy() {
        Path store = Paths.get("/tmp/store" + System.currentTimeMillis());
        System.out.println(store);
        VirtualMachinesStore pm = new VirtualMachinesStore(store);
        VirtualMachineEntity entity1 = new VirtualMachineEntity();
        entity1.setHomeFolder("/tmp/lol");
        entity1.setSocket("/tmp/sock");
        pm.add(entity1);

        VirtualMachineEntity entity2 = new VirtualMachineEntity();
        entity2.setHomeFolder("/tmp/lol2");
        entity2.setSocket("/tmp/sock1");
        pm.add(entity2);

        pm.save();

        pm = new VirtualMachinesStore(store);
        var vms = pm.findAll();
        Assertions.assertEquals(1, vms.size());
        Assertions.assertEquals("id: null\nhome folder: /tmp/lol\nsocket: /tmp/sock\nstate: null\npid: null\n",
            vms.stream().findFirst().get().toString());
    }
}
