package id.fireguard;

import static java.lang.System.exit;
import static java.lang.System.out;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

import id.fireguard.vmm.VirtualMachinesStore;
import id.fireguard.vmm.VirtualMachine;
import id.fireguard.vmm.VirtualMachineManager;

public class FireGuardApp {

	private VirtualMachineManager vmm;

    public FireGuardApp(VirtualMachineManager vmm) {
		this.vmm = vmm;
	}

	private static void usage() throws IOException {
        try (Scanner scanner = new Scanner(FireGuardApp.class.getResource("usage.txt").openStream())
                .useDelimiter("\n")) {
            while (scanner.hasNext())
                out.println(scanner.next());
        }
    }

	public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            usage();
            exit(1);
        }
        var settings = Settings.load();
        var pm = VirtualMachinesStore.load(settings.getStore());
        setup(settings.getStage());

        var dbcm = new VirtualMachineManager(settings, pm);
        var app = new FireGuardApp(dbcm);
        var cmd = args[0];
        switch (cmd) {
        case "create": app.create(args[1]); break;
        case "showAll": app.showAll(); break;
        case "start": app.start(args[1]); break;
        default: usage();
        }
        
	}

	private void showAll() {
		vmm.findAll().forEach(out::println);
	}

	private void start(String vmId) {
		out.format("Starting VM with id %s...\n", vmId);
		vmm.start(vmId);
	}

	private static void setup(Path stage) {
		File f = stage.toFile();
		if (f.exists()) return;
		f.mkdirs();
	}

	private void create(String ip) {
		out.println("Creating new VM...");
		VirtualMachine dbc = vmm.create(ip);
		out.println(dbc);
	}


}
