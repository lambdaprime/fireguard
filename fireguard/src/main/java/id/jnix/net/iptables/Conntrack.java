/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.net.iptables;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Conntrack implements Module {

    private static final long serialVersionUID = 1L;

    public enum State {
        RELATED,ESTABLISHED;
    }

    private List<State> ctstate = List.of();

    public Conntrack withStates(List<State> states) {
        ctstate = states;
        return this;
    }

    @Override
    public List<String> getArgs() {
        var args = new ArrayList<String>();
        args.add("-m");
        args.add("conntrack");
        if (!ctstate.isEmpty()) {
            args.add("--ctstate");
            args.add(ctstate.stream().
                    map(Enum::name)
                    .collect(Collectors.joining(",")));
        }
        if (args.size() == 2) return List.of();
        return args;
    }

}
