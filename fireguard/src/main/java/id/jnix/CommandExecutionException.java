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

import static java.lang.String.format;

import id.xfunction.lang.XProcess;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class CommandExecutionException extends Exception {

    private static final long serialVersionUID = 1L;

    public CommandExecutionException(String message) {
        super(message);
    }

    public CommandExecutionException(Exception cause) {
        super(cause);
    }

    public CommandExecutionException(XProcess proc) {
        super(
                format(
                        "Command %s failed with message:\n%s\n%s",
                        proc.process().info().commandLine().orElse(""),
                        proc.stderr(),
                        proc.stdout()));
    }
}
