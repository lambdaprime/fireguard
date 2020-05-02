/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.net.iptables;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.stream.Collectors;

import id.jnix.CommandHasSudo;
import id.xfunction.XExec;
import id.xfunction.XProcess;
import id.xfunction.function.Unchecked;

public class IpTables extends CommandHasSudo<IpTables> {

    public void add(Rule rule) {
        var cmd = new ArrayList<String>();
        sudo(cmd);
        cmd.add("iptables");
        rule.getTable().ifPresent(t -> {
            cmd.add("-t " + t);
        });
        cmd.add("-A " + rule.getChain().toString());
        rule.getInIface().ifPresent(in -> {
            cmd.add("-i " + in);
        });
        rule.getOutIface().ifPresent(out -> {
            cmd.add("-o " + out);
        });
        rule.getModule().ifPresent(out -> {
            cmd.addAll(out.getArgs());
        });
        cmd.add("-j " + rule.getTarget().toString());
        XProcess proc = run(cmd.stream()
                .collect(Collectors.joining(" ")));
        if (Unchecked.getInt(() -> proc.code().get()) != 0) {
            throw new RuntimeException(format("Command [%s] failed with message:\n%s\n%s",
                    cmd, proc.stderrAsString(), proc.stdoutAsString()));
        }
    }

    protected XProcess run(String cmd) {
        return new XExec(cmd).run();
    }

    @Override
    protected IpTables self() {
        return this;
    }

}
