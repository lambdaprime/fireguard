/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.app;

import java.util.List;

public interface Command {

    void execute(List<String> positionalArgs) throws CommandIllegalArgumentException;

}
