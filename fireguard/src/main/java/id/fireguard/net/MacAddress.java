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
package id.fireguard.net;

import id.xfunction.Preconditions;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class MacAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    private long value;

    public MacAddress(long value) {
        this.value = value;
    }

    public byte[] getBytes() {
        byte[] a = new byte[6];
        long m = 0xff;
        var v = value;
        for (int i = a.length - 1; i >= 0; i--) {
            a[i] = (byte) (v & m);
            v >>= 8;
        }
        return a;
    }

    public long getValue() {
        return value;
    }

    public MacAddress inc() {
        return new MacAddress(value + 1);
    }

    public static MacAddress parseMac(String mac) {
        String[] a = mac.split(":");
        Preconditions.isTrue(a.length == 6, "Wrong MAC address format");
        long v = 0;
        for (int i = 0; i < a.length; i++) {
            v <<= 8;
            v = v | Integer.valueOf(a[i], 16);
        }
        return new MacAddress(v);
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
        MacAddress r = (MacAddress) obj;
        return Objects.equals(value, r.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        var sj = new StringJoiner(":");
        for (byte i : getBytes()) {
            var t = Integer.toHexString(Byte.toUnsignedInt(i));
            t = t.toUpperCase();
            t = "0".repeat(2 - t.length()) + t;
            sj.add(t);
        }
        return sj.toString();
    }
}
