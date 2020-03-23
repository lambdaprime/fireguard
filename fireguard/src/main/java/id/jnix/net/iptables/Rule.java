package id.jnix.net.iptables;

import java.io.Serializable;
import java.util.Optional;

public class Rule implements Serializable{
	private static final long serialVersionUID = 1L;

	Optional<Table> table = Optional.empty();
	Chain chain;
	Optional<String> inIface = Optional.empty();
	Optional<String> outIface = Optional.empty();
	Target target;
	
	public Rule(Chain chain, Target target) {
		this.chain = chain;
		this.target = target;
	}

	public Rule withTable(Table t) {
		table = Optional.of(t);
		return this;
	}

	public Rule withInIface(String s) {
		inIface = Optional.of(s);
		return this;
	}
	
	public Rule withOutIface(String s) {
		outIface = Optional.of(s);
		return this;
	}
}
