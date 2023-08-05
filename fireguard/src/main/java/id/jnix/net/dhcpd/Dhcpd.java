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
package id.jnix.net.dhcpd;

import id.jnix.CommandExecutionException;
import id.jnix.CommandHasSudo;
import id.jnix.internal.Utils;
import id.xfunction.lang.XExec;
import id.xfunction.lang.XProcess;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Requires sudo access to /etc/init.d/isc-dhcp-server and write permission to /etc/dhcp/dhcpd.conf
 */
public class Dhcpd extends CommandHasSudo<Dhcpd> {

    private static final Path configFile = Paths.get("/etc/dhcp/dhcpd.conf");
    private Utils utils = new Utils();

    public Dhcpd() {
        withSudo();
    }

    public Process start(DhcpdConfig conf) throws CommandExecutionException, IOException {
        Files.write(configFile, conf.toString().getBytes());
        return run("start").process();
    }

    /**
     * @author lambdaprime intid@protonmail.com
     */
    public void stop() throws CommandExecutionException {
        run("stop");
    }

    public boolean isRunning() throws CommandExecutionException {
        return run("status").await() == 0;
    }

    private XProcess run(String command) throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        sudo(cmd);
        cmd.add("/etc/init.d/isc-dhcp-server");
        cmd.add(command);
        XProcess proc = new XExec(cmd).start();
        utils.verifyCode(proc);
        return proc;
    }

    @Override
    protected Dhcpd self() {
        return this;
    }
}
