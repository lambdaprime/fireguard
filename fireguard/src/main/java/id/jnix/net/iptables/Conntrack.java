/*
 * Copyright 2020 fireguard project
 * 
 * Website: https://github.com/lambdaprime/fireguard
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.jnix.net.iptables;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class Conntrack implements Module {

    private static final long serialVersionUID = 1L;

    public enum State {
        RELATED,
        ESTABLISHED;
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
            args.add(ctstate.stream().map(Enum::name).collect(Collectors.joining(",")));
        }
        if (args.size() == 2) return List.of();
        return args;
    }
}
