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
package id.jnix.net.ip;

import java.util.Objects;
import java.util.Optional;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class Route {

    public static final String DEFAULT_DESTINATION = "default";

    private String destination;
    private String device;
    private int mask;

    public Route(String destination, String device) {
        this(destination, device, 32);
    }

    public Route(String destination, String device, int mask) {
        this.destination = destination;
        this.device = device;
        this.mask = mask;
    }

    public String getDestination() {
        return destination;
    }

    public String getDevice() {
        return device;
    }

    public int getMask() {
        return mask;
    }

    static Optional<Route> parse(String line) {
        var a = line.split("\\s+");
        if (line.length() < 3) return Optional.empty();
        return Optional.of(new Route(a[0], a[2]));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Route r = (Route) obj;
        return Objects.equals(destination, r.destination) && Objects.equals(device, r.device);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination, device);
    }

    @Override
    public String toString() {
        return String.format("destination: %s/%d, device: %s", destination, mask, device);
    }
}
