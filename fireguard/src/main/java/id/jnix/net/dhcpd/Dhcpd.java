package id.jnix.net.dhcpd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// Can't open /tmp/dhcpd105391253209477051: Permission denied
// apparmor/selinux
// sudo ln -s /etc/apparmor.d/usr.sbin.dhcpd /etc/apparmor.d/disable/
// sudo apparmor_parser -R /etc/apparmor.d/usr.sbin.dhcpd 
public class Dhcpd {

	public Process start(DhcpdConfig conf) throws Exception {
		Path configFile = Files.createTempFile("dhcpd", "");
		Files.write(configFile, conf.toString().getBytes());
		return run(configFile);
	}

	private Process run(Path configFile) throws IOException {
        var pb = new ProcessBuilder("sudo",
        		"dhcpd",
                "-cf",
                configFile.toString());
        return pb.start();
	}

}
