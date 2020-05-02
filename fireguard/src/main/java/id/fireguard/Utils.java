/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard;

import id.xfunction.XAsserts;
import id.xfunction.function.Unchecked;

public class Utils {

    public void killAndWait(ProcessHandle ph) {
        ph.children().forEach(this::killAndWait);
        boolean isDestroyed = ph.destroy();
        if (!isDestroyed)
            isDestroyed = ph.destroyForcibly();
        XAsserts.assertTrue(isDestroyed, "Could not terminate process " + ph.pid());
        Unchecked.run(() -> ph.onExit().get());
    }

}
