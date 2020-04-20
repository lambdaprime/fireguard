/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.net.ip;

public class Address {

    private String deviceName;

    public Address(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public String toString() {
        return String.format("deviceName: %s", deviceName);
    }
}
