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

import static org.junit.jupiter.api.Assertions.assertEquals;

import id.fireguard.vmm.VirtualMachineEntity;
import id.fireguard.vmm.VirtualMachinesStore;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

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
        assertEquals(1, vms.size());
        assertEquals(
                "id: null\nhome folder: /tmp/lol\nsocket: /tmp/sock\nstate: null\npid: null\n",
                vms.stream().findFirst().get().toString());
    }
}
