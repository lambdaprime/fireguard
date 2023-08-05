/*
 * Copyright 2020 fireguard project
 * 
 * Website: https://github.com/lambdaprime/fireguard
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.fireguard.app;

import id.fireguard.vmm.VirtualMachine;
import id.fireguard.vmm.VirtualMachineManager;
import id.xfunction.cli.CommandLineInterface;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class VmCommand implements Command {

    private VirtualMachineManager vmm;
    private CommandLineInterface cli;

    public VmCommand(VirtualMachineManager vmm, CommandLineInterface cli) {
        this.vmm = vmm;
        this.cli = cli;
    }

    private void startAll() {
        vmm.findAll().stream().map(VirtualMachine::getId).sorted().forEach(this::start);
    }

    private void stopAll() {
        vmm.findAll().stream().map(VirtualMachine::getId).sorted().forEach(this::stop);
    }

    private void stop(String vmId) {
        cli.print("Stopping VM with id %s...\n", vmId);
        vmm.stop(vmId);
    }

    private void showAll() {
        vmm.findAll().stream()
                .sorted(Comparator.comparing(VirtualMachine::getId))
                .forEach(cli::print);
    }

    private void start(String vmId) {
        cli.print("Starting VM with id %s...\n", vmId);
        vmm.start(vmId);
    }

    private void restart(String vmId) {
        stop(vmId);
        start(vmId);
    }

    private void create(Optional<String> jqExpr) {
        cli.print("Creating new VM...");
        VirtualMachine vm = vmm.create(jqExpr);
        cli.print(vm);
    }

    private void update(String vmId, String jqExpr) {
        cli.print("Updating VM with id " + vmId);
        var vm = vmm.update(vmId, jqExpr);
        cli.print(vm);
    }

    @Override
    public void execute(List<String> positionalArgs) throws CommandIllegalArgumentException {
        var cmd = positionalArgs.remove(0);
        switch (cmd) {
            case "create":
                create(positionalArgs.stream().findAny());
                break;
            case "showAll":
                showAll();
                break;
            case "startAll":
                startAll();
                break;
            case "stopAll":
                stopAll();
                break;
            case "restart":
                restart(positionalArgs.get(0));
                break;
            case "update":
                update(positionalArgs.get(0), positionalArgs.get(1));
                break;
            case "start":
                start(positionalArgs.get(0));
                break;
            case "stop":
                stop(positionalArgs.get(0));
                break;
            default:
                throw new CommandIllegalArgumentException();
        }
    }
}
