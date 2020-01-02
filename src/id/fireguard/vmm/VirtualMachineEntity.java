package id.fireguard.vmm;

import static java.lang.String.format;

import java.io.Serializable;
import java.util.Objects;

public class VirtualMachineEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	String id;
	String homeFolder;
	String socket;
	State state;

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
        VirtualMachineEntity r = (VirtualMachineEntity) obj;
		return Objects.equals(id, r.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		var sb = new StringBuilder();
		sb.append(format("id: %s\n", id));
		sb.append(format("home folder: %s\n", homeFolder));
		sb.append(format("socket: %s\n", socket));
		sb.append(format("state: %s\n", state));
		return sb.toString();
	}
}
