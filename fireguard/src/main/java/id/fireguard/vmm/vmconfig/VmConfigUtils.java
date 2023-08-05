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
package id.fireguard.vmm.vmconfig;

import id.jnix.Jq;
import id.xfunction.lang.XProcess;
import java.nio.file.Path;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class VmConfigUtils {

    private static final String VM_CONFIG_JSON = "vm_config.json";
    private final VmConfigJsonUtils vmJsonUtils;

    public VmConfigUtils() {
        this(new VmConfigJsonUtils());
    }

    public VmConfigUtils(VmConfigJsonUtils vmJsonUtils) {
        this.vmJsonUtils = vmJsonUtils;
    }

    public VmConfig load(Path vmHome) {
        Path vmConfigPath = vmHome.resolve(VM_CONFIG_JSON);
        var vmConfig = new VmConfig(vmConfigPath, vmJsonUtils.load(vmConfigPath));
        return vmConfig;
    }

    public void save(VmConfig vmConfig) {
        vmJsonUtils.save(vmConfig.getLocation(), vmConfig.getVmConfigJson());
    }

    public void update(Path vmHome, String jqExpr) {
        Path vmConfigPath = vmHome.resolve(VM_CONFIG_JSON);
        try {
            var proc =
                    new XProcess(
                            new Jq()
                                    .withFile(vmConfigPath)
                                    .withFilter(jqExpr)
                                    .withInplaceMode()
                                    .run());
            var ret = proc.code().get();
            if (ret != 0) {
                throw new RuntimeException(proc.stderr());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
