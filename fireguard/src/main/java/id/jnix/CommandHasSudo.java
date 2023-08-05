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
package id.jnix;

import java.util.List;

/**
 * @author lambdaprime intid@protonmail.com
 */
public abstract class CommandHasSudo<T> {

    protected boolean withSudo;

    /** Requires sudo permission to the command Otherwise call will fail with 'Permission denied' */
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
