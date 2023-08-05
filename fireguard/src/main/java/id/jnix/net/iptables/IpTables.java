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
package id.jnix.net.iptables;

import id.jnix.CommandExecutionException;
import id.jnix.CommandHasSudo;
import id.jnix.internal.Utils;
import id.xfunction.lang.XExec;
import id.xfunction.lang.XProcess;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class IpTables extends CommandHasSudo<IpTables> {

    private Utils utils = new Utils();

    public void add(Rule rule) throws CommandExecutionException {
        var cmd = cmdLine("-A", rule);
        XProcess proc = run(cmd);
        utils.verifyCode(proc);
    }

    /** Checks if given rule already exist in iptables or not */
    public boolean isPresent(Rule rule) {
        var cmd = cmdLine("-C", rule);
        XProcess proc = run(cmd);
        return proc.await() == 0;
    }

    protected XProcess run(String cmd) {
        return new XExec(cmd).start();
    }

    private String cmdLine(String actionArg, Rule rule) {
        var cmd = new ArrayList<String>();
        sudo(cmd);
        cmd.add("iptables");
        rule.getTable()
                .ifPresent(
                        t -> {
                            cmd.add("-t " + t);
                        });
        cmd.add(actionArg + " " + rule.getChain().toString());
        rule.getInIface()
                .ifPresent(
                        in -> {
                            cmd.add("-i " + in);
                        });
        rule.getOutIface()
                .ifPresent(
                        out -> {
                            cmd.add("-o " + out);
                        });
        rule.getModule()
                .ifPresent(
                        out -> {
                            cmd.addAll(out.getArgs());
                        });
        cmd.add("-j " + rule.getTarget().toString());
        return cmd.stream().collect(Collectors.joining(" "));
    }

    @Override
    protected IpTables self() {
        return this;
    }
}
