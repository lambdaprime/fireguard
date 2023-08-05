/*
 * Copyright 2023 fireguard project
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
package id.jnix.internal;

import id.jnix.CommandExecutionException;
import id.xfunction.lang.XProcess;
import java.util.concurrent.ExecutionException;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class Utils {

    public void verifyCode(XProcess proc) throws CommandExecutionException {
        try {
            if (proc.code().get() != 0) throw new CommandExecutionException(proc);
        } catch (InterruptedException | ExecutionException e) {
            var ex = new CommandExecutionException(proc);
            ex.addSuppressed(e);
            throw ex;
        }
    }
}
