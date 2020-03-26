package id.fireguard;

import java.util.List;

public interface Command {

	void execute(List<String> positionalArgs) throws CommandIllegalArgumentException;

}
