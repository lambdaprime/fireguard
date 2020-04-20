/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.net.iptables;

import java.io.Serializable;
import java.util.Optional;

public class Rule implements Serializable {
    private static final long serialVersionUID = 1L;

    private Optional<Table> table = Optional.empty();
    private Chain chain;
    private Optional<String> inIface = Optional.empty();
    private Optional<String> outIface = Optional.empty();
    private Optional<Module> module = Optional.empty();
    private Target target;

    public Rule(Chain chain, Target target) {
        this.chain = chain;
        this.target = target;
    }

    public Rule withTable(Table t) {
        table = Optional.of(t);
        return this;
    }

    public Rule withInIface(String s) {
        inIface = Optional.of(s);
        return this;
    }

    public Rule withOutIface(String s) {
        outIface = Optional.of(s);
        return this;
    }

    public Rule withModule(Module m) {
        module = Optional.of(m);
        return this;
    }
    
    public Optional<Table> getTable() {
        return table;
    }
    
    public Chain getChain() {
        return chain;
    }
    
    public Target getTarget() {
        return target;
    }
    
    public Optional<String> getInIface() {
        return inIface;
    }
    
    public Optional<String> getOutIface() {
        return outIface;
    }
    
    public Optional<Module> getModule() {
        return module;
    }
    
}
