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

import java.io.Serializable;
import java.util.Optional;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class Rule implements Serializable {
    private static final long serialVersionUID = 1L;

    private Optional<Table> table = Optional.empty();
    private Chain chain;
    private Optional<String> inIface = Optional.empty();
    private Optional<String> outIface = Optional.empty();
    private Optional<Module> module = Optional.empty();
    private Optional<String> sourceAddr = Optional.empty();
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

    public Rule withSource(String addr) {
        sourceAddr = Optional.of(addr);
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

    public Optional<String> getSourceAddr() {
        return sourceAddr;
    }
}
