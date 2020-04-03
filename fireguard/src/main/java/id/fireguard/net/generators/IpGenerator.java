package id.fireguard.net.generators;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import id.xfunction.function.Unchecked;

public class IpGenerator {

	private Set<InetAddress> ips;
	
	public IpGenerator(Set<InetAddress> ips) {
		this.ips = new HashSet<>(ips);
	}

	public Optional<InetAddress> newIp(InetAddress subnet) {
		var ip = inc(subnet);
		if (ip.isEmpty()) return Optional.empty();
		var sorted = ips.stream()
			.sorted(this::cmpIp)
			.collect(Collectors.toList());
		var iter = sorted.iterator();
		while (iter.hasNext()) {
			if (!ip.get().equals(iter.next())) break;
			ip = inc(ip.get());
			if (ip.isEmpty()) return Optional.empty();
		}
		if (!valid(subnet, ip.get())) {
			return Optional.empty();
		}
		ips.add(ip.get());
		return ip;
	}
	
	protected boolean valid(InetAddress subnet, InetAddress ip) {
		var sb = subnet.getAddress();
		var ipb = ip.getAddress();
		for (int i = 0; i < 4; i++) {
			if (sb[i] == 0) continue;
			if (sb[i] != ipb[i]) return false;
		}
		return true;
	}

	protected Optional<InetAddress> inc(InetAddress ip) {
		var b = ip.getAddress();
		for (int i = 3; i >= 0; i--) {
			if (Byte.compareUnsigned(b[i], (byte)254) >= 0) {
				b[i] = 1;
				continue;
			}
			b[i]++;
			return Unchecked.get(() -> Optional.of(InetAddress.getByAddress(b)));
		}
		return Optional.empty();
	}

	protected int cmpIp(InetAddress a, InetAddress b) {
		return ByteBuffer.wrap(a.getAddress()).getInt() - ByteBuffer.wrap(b.getAddress()).getInt();
	}
}
