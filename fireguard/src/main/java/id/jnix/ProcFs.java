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
package id.jnix;

import id.jnix.internal.Utils;
import id.xfunction.lang.XExec;
import id.xfunction.lang.XProcess;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class ProcFs {

    private Utils utils = new Utils();

    /**
     * Returns value from the given file as an integer:
     *
     * <pre>{@code
     * readInt(Paths.get("/proc/sys/net/ipv4/ip_forward"))
     * }</pre>
     *
     * <p>Will return:
     *
     * <ul>
     *   <li>0 - if forwarding disable
     *   <li>1 - if it is enabled
     * </ul>
     *
     * @throws CommandExecutionException
     */
    public int readInt(Path path) throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        cmd.add("cat");
        cmd.add(path.toString());
        XProcess proc = new XExec(cmd).start();
        utils.verifyCode(proc);
        return Integer.parseInt(proc.stdout());
    }
}
