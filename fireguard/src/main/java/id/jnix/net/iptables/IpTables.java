/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.net.iptables;

import java.util.ArrayList;
import java.util.stream.Collectors;

import id.jnix.CommandExecutionException;
import id.jnix.CommandHasSudo;
import id.jnix.internal.Utils;
import id.xfunction.XExec;
import id.xfunction.XProcess;

public class IpTables extends CommandHasSudo<IpTables> {

    private Utils utils = new Utils();

    public void add(Rule rule) throws CommandExecutionException {
        var cmd = cmdLine("-A", rule);
        XProcess proc = run(cmd);
        utils.verifyCode(proc);
    }

    /**
     * Checks if given rule already exist in iptables or not
     */
    public boolean isPresent(Rule rule) {
        var cmd = cmdLine("-C", rule);
        XProcess proc = run(cmd);
        return proc.getCode() == 0;
    }

    protected XProcess run(String cmd) {
        return new XExec(cmd).run();
    }

    private String cmdLine(String actionArg, Rule rule) {
        var cmd = new ArrayList<String>();
        sudo(cmd);
        cmd.add("iptables");
        rule.getTable().ifPresent(t -> {
            cmd.add("-t " + t);
        });
        cmd.add(actionArg + " " + rule.getChain().toString());
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
        return cmd.stream()
                .collect(Collectors.joining(" "));
    }

    @Override
    protected IpTables self() {
        return this;
    }

}
