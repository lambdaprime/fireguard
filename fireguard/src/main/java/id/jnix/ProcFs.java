/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix;

import java.nio.file.Path;
import java.util.ArrayList;

import id.jnix.internal.Utils;
import id.xfunction.XExec;
import id.xfunction.XProcess;

public class ProcFs {

    private Utils utils = new Utils();

    /**
     * <p>Returns value from the given file as an integer:</p>
     * 
     * <pre>{@code 
     * readInt(Paths.get("/proc/sys/net/ipv4/ip_forward"))
     * }</pre>
     * 
     * <p>Will return:</p>
     * 
     * <ul>
     * <li>0 - if forwarding disable</li>
     * <li>1 - if it is enabled</li>
     * </ul>
     * @throws CommandExecutionException 
     * 
     */
    public int readInt(Path path) throws CommandExecutionException {
        var cmd = new ArrayList<String>();
        cmd.add("cat");
        cmd.add(path.toString());
        XProcess proc = new XExec(cmd)
                .run();
        utils.verifyCode(proc);
        return Integer.parseInt(proc.stdoutAsString());
    }

}
