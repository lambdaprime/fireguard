/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.net.ip;

import java.util.Objects;
import java.util.Optional;

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
        return Objects.equals(destination, r.destination) &&
                Objects.equals(device, r.device);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination, device);
    }

    @Override
    public String toString() {
        return String.format("destination: %s/%d, device: %s",
                destination, mask, device);
    }
}
