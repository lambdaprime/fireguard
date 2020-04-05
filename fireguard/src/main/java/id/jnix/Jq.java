package id.jnix;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import id.xfunction.XAsserts;
import id.xfunction.XExec;
import id.xfunction.function.Unchecked;

/**
 * Provides an access to jq (Command-line JSON processor) and
 * 
 * - allows to pass JSON (or anything else) as an input
 * to jq directly (no need to save it to the file)
 * 
 * - supports inplace replace in the files (currently jq does
 * not allow that)
 * 
 */
public class Jq {

	private List<String> options = List.of();
	private String filter;
	private Optional<Stream<String>> inputOpt = Optional.empty();
	private Optional<Path> fileOpt = Optional.empty();

	private boolean withSudo;
	private boolean withInplace;

	public Jq withSudo() {
		this.withSudo = true;
		return this;
	}

	public Jq withInplaceMode() {
		this.withInplace = true;
		return this;
	}
	
	public Jq withOptions(List<String> options) {
		this.options = options;
		return this;
	}
	
	public Jq withFilter(String filter) {
		this.filter = filter;
		return this;
	}

	public Jq withInput(Stream<String> input) {
		this.inputOpt = Optional.of(input);
		return this;
	}
	
	public Jq withFile(Path file) {
		this.fileOpt = Optional.of(file);
		return this;
	}
	
	public Process run() throws Exception {
		if (withInplace) {
			XAsserts.assertTrue(fileOpt.isPresent(), "Inplace mode requires file to be set");
			XAsserts.assertTrue(inputOpt.isEmpty(), "Inplace mode accepts input from file not from stdin");
		}
		var cmd = new ArrayList<String>();
		if (withSudo)
			cmd.add("sudo");
		cmd.add("jq");
		cmd.addAll(options);
		cmd.add(filter);
		if (fileOpt.isPresent())
			cmd.add(fileOpt.get().toString());
        var pb = new XExec(cmd.toArray(new String[0]));
        if (inputOpt.isPresent())
			pb.withInput(inputOpt.get());
        var proc = pb.run().process();
        if (withInplace) {
        	Path file = fileOpt.get();
	        var tmpFile = Files.createTempFile(file.getFileName().toString(), "");
	        proc.getInputStream().transferTo(
	        		Files.newOutputStream(tmpFile, StandardOpenOption.WRITE));
	        proc.onExit().thenAccept(
	        		Unchecked.wrapAccept(c -> {
	        			if (proc.exitValue() == 0) Files.move(tmpFile, file, StandardCopyOption.REPLACE_EXISTING);
	        }));
        }
        return proc;
	}
}
