/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.tests.vmm.vmconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import id.fireguard.vmm.vmconfig.NetworkIface;
import id.fireguard.vmm.vmconfig.VmConfigJsonUtils;

public class VmConfigJsonUtilsTest {

    @Test
    public void test_load() throws Exception {
        var vmconfig = new VmConfigJsonUtils().load(Paths.get(getClass()
                .getResource("vm_config.json").toURI()));
        List<NetworkIface> ifaces = vmconfig.getNetworkInterfaces();
        assertEquals(1, ifaces.size());
        assertEquals("tap3", ifaces.get(0).getHostDevName());
    }
}
