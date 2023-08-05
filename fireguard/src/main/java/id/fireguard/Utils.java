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
package id.fireguard;

import id.xfunction.lang.XRE;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class Utils {

    public void killAndWait(ProcessHandle ph) {
        ph.children().forEach(this::killAndWait);
        boolean isDestroyed = ph.destroy();
        if (!isDestroyed) isDestroyed = ph.destroyForcibly();
        try {
            ph.onExit().get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new XRE("Could not terminate process %d: %s", ph.pid(), e.getMessage());
        }
    }
}
