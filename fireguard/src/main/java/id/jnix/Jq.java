/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jnix;

import static id.xfunction.XAsserts.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import id.xfunction.XExec;
import id.xfunction.XProcess;

/**
 * Provides an access to jq (Command-line JSON processor) and
 * <ul>
 * <li>allows to pass JSON (or anything else) as an input
 * to jq directly (no need to save it to the file)</li>
 * 
 * <li>supports inplace replace in the files (currently jq does
 * not allow that)</li>
 * </ul>
 */
public class Jq extends CommandHasSudo<Jq> {

    private List<String> options = List.of();
    private String filter;
    private Optional<Stream<String>> inputOpt = Optional.empty();
    private Optional<Path> fileOpt = Optional.empty();

    private boolean withInplace;

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

    /**
     * This makes an async call to jq and returns the Process to it.
     * 
     * <p>With inplace mode this call will be sync and will block until
     * jq will not finish. This is because in inplace we need to
     * replace the file at the end. Making it async will cause
     * racing problems when you do many inplace changes on the same
     * file.</p>
     */
    public Process run() throws Exception {
        if (withInplace) {
            assertTrue(fileOpt.isPresent(), "Inplace mode requires file to be set");
            assertTrue(inputOpt.isEmpty(), "Inplace mode accepts input from file not from stdin");
        }
        var cmd = new ArrayList<String>();
        sudo(cmd);
        cmd.add("jq");
        cmd.addAll(options);
        cmd.add(filter);
        if (fileOpt.isPresent())
            cmd.add(fileOpt.get().toString());
        var pb = new XExec(cmd);
        if (inputOpt.isPresent())
            pb.withInput(inputOpt.get());
        var proc = pb.run().process();
        if (withInplace) {
            Path file = fileOpt.get();
            var tmpFile = Files.createTempFile(file.getFileName().toString(), "");
            proc.getInputStream().transferTo(
                    Files.newOutputStream(tmpFile, StandardOpenOption.WRITE));
            if (new XProcess(proc).code().get() == 0) 
                Files.move(tmpFile, file, StandardCopyOption.REPLACE_EXISTING);
            else
                Files.delete(tmpFile);
        }
        return proc;
    }

    @Override
    protected Jq self() {
        return this;
    }
}
