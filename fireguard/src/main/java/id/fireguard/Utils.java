/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import id.xfunction.XUtils;

public class Utils {

    public void killAndWait(ProcessHandle ph) {
        ph.children().forEach(this::killAndWait);
        boolean isDestroyed = ph.destroy();
        if (!isDestroyed)
            isDestroyed = ph.destroyForcibly();
        try {
            ph.onExit().get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            XUtils.throwRuntime("Could not terminate process %d: %s", ph.pid(), e.getMessage());
        }
    }

}
