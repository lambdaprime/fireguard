package id.fireguard;

import java.util.LinkedList;

public interface Command {

	void execute(LinkedList<String> positionalArgs) throws CommandIllegalArgumentException;

}
