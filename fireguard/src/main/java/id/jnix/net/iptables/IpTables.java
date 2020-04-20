/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.net.iptables;

import static java.lang.String.format;

import java.util.StringJoiner;

import id.xfunction.XExec;
import id.xfunction.XProcess;
import id.xfunction.function.Unchecked;

public class IpTables {

    private boolean withSudo;

    public IpTables withSudo() {
        this.withSudo = true;
        return this;
    }

    public void add(Rule rule) {
        var cmd = new StringJoiner(" ");
        if (withSudo)
            cmd.add("sudo");
        cmd.add("iptables");
        rule.table.ifPresent(t -> {
            cmd.add("-t " + t);
        });
        cmd.add("-A " + rule.chain.toString());
        rule.inIface.ifPresent(in -> {
            cmd.add("-i " + in);
        });
        rule.outIface.ifPresent(out -> {
            cmd.add("-o " + out);
        });
        cmd.add("-j " + rule.target.toString());
        XProcess proc = run(cmd.toString());
        if (Unchecked.getInt(() -> proc.code().get()) != 0) {
            throw new RuntimeException(format("Command [%s] failed with message:\n%s\n%s",
                    cmd, proc.stderrAsString(), proc.stdoutAsString()));
        }
    }

    private void verify(XProcess proc) {
        if (Unchecked.getInt(() -> proc.code().get()) != 0) {
            throw new RuntimeException(format("Command [%s] failed with message:\n%s\n%s",
                    proc.process().info().commandLine().get(), proc.stderrAsString(),
                    proc.stdoutAsString()));
        }
    }

    protected XProcess run(String cmd) {
        return new XExec(cmd).run();
    }
}
