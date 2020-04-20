/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix;

import static java.lang.String.format;

import id.xfunction.XProcess;

public class CommandExecutionException extends Exception {

    private static final long serialVersionUID = 1L;

    public CommandExecutionException(String message) {
        super(message);
    }

    public CommandExecutionException(Exception cause) {
        super(cause);
    }

    public CommandExecutionException(XProcess proc) {
        super(format("Command %s failed with message:\n%s\n%s",
                proc.process().info().commandLine().orElse(""), proc.stderrAsString(),
                proc.stdoutAsString()));
    }
}
