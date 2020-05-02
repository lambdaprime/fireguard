/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix;

import java.util.List;

public abstract class CommandHasSudo<T> {

    protected boolean withSudo;

    /**
     * Requires sudo permission to the command
     * Otherwise call will fail with 'Permission denied'
     */
    public T withSudo() {
        this.withSudo = true;
        return self();
    }

    protected abstract T self();
    
    protected void sudo(List<String> cmd) {
        if (withSudo) {
            cmd.add("sudo");
        }
    }
}
