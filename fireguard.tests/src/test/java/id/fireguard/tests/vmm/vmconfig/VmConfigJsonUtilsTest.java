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
package id.fireguard.tests.vmm.vmconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

import id.fireguard.vmm.vmconfig.NetworkIface;
import id.fireguard.vmm.vmconfig.VmConfigJsonUtils;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

public class VmConfigJsonUtilsTest {

    @Test
    public void test_load() throws Exception {
        var vmconfig =
                new VmConfigJsonUtils()
                        .load(
                                Paths.get(
                                        ClassLoader.getSystemResource(
                                                        "id/fireguard/tests/vmm/vmconfig/vm_config.json")
                                                .toURI()));
        List<NetworkIface> ifaces = vmconfig.getNetworkInterfaces();
        assertEquals(1, ifaces.size());
        assertEquals("tap3", ifaces.get(0).getHostDevName());
    }
}
