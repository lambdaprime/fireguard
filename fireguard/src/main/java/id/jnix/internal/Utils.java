package id.jnix.internal;

import java.util.concurrent.ExecutionException;

import id.jnix.CommandExecutionException;
import id.xfunction.XProcess;

public class Utils {

    public void verifyCode(XProcess proc) throws CommandExecutionException {
        try {
            if (proc.code().get() != 0)
                throw new CommandExecutionException(proc);
        } catch (InterruptedException | ExecutionException e) {
            var ex = new CommandExecutionException(proc);
            ex.addSuppressed(e);
            throw ex;
        }
    }

}
