/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix.net.iptables;

import java.io.Serializable;
import java.util.List;

public interface Module extends Serializable {
    List<String> getArgs();
}
