fireguard - Firecracker MicroVMs management application

lambdaprime <id.blackmesa@gmail.com>

# Quick Start

See [Quick Start](/QuickStart.md) for more details.

# Requirements

- Java 11
- Screen version 4.06.02 (GNU) 23-Oct-17
- firecracker v0.20.0
- jq-1.5-1-a5b5cbe

# Download

You can download **fireguard** from <https://github.com/lambdaprime/fireguard/tree/master/release>

# Configuration

Before using **fireguard** make sure to create configuration file .fireguard and place it in your $HOME location:

```
store = <STORE_LOCATION>
originVm = <ORIGIN_VM_LOCATION>
stage = <STAGE_LOCATION>
firecracker = <FIRECRACKER_LOCATION>
```

Where:

STORE\_LOCATION -- directory where **fireguard** will keep metadata about VMs

ORIGIN\_VM\_LOCATION -- directory with original VM. It should be self sufficient and include everything what is needed to run a VM (kernel, vm_config.json, etc). See Quick Start for more details.

STAGE_LOCATION -- directory where **fireguard** will store all managed VMs (each VM will be stored in its own subdirectory)

FIRECRACKER\_LOCATION -- path to Firecracker

# Usage

```bash
fireguard <COMMAND>
```

COMMAND is one of the following:

- create JQ\_EXPRESSION -- create a new VM by copying ORIGIN to a STAGE and updating its vm\_config.json with accordance to JQ_EXPRESSION
- showAll -- show information about all available VMs
- startAll -- start all VMs
- stopAll -- stop all VMs (right now it is done by killing the process)
- restart VM\_ID -- restart VM with given id
- update VM\_ID JQ\_EXPRESSION -- update vm\_config.json using jq with JQ\_EXPRESSION
- start VM\_ID -- start a VM with given id
- stop VM\_ID -- stop a VM with given id

Where:

JQ\_EXPRESSION -- it is a jq expression which will be sent to jq together with vm\_config.json. It allows you to change certain VM configuration parameters.

# Examples

## Configuration file

```
store = /home/ubuntu/vms/store
originVm = /home/ubuntu/opt/mysql.origin
stage = /home/ubuntu/vms/stage
firecracker = /home/ubuntu/opt/firecracker/firecracker-v0.20.0-x86_64
```

## Usage

Create new VM:

```bash 
% fireguard create '."network-interfaces"[0].guest_mac = "AA:FC:00:00:00:01" | ."network-interfaces"[0].host_dev_name = "tap1"'
Creating new VM...
id: vm-1
home folder: /home/ubuntu/vms/stage/vm-1
socket: /home/ubuntu/vms/stage/vm-1/firecracker.sock
state: STOPPED
pid: Optional.empty
vmConfig: path: /home/ubuntu/vms/stage/vm-1/vm_config.json
memoryGb: 8
vcpu: 1
hostIface: Optional.empty
mac: Optional.empty
%
```

Start a VM:

```bash
% fireguard start vm-1
Starting VM with id vm-1...
%
```

Show all available VMs:

```bash
% fireguard showAll   
id: vm-2
home folder: /home/ubuntu/vms/stage/vm-2
socket: /home/ubuntu/vms/stage/vm-2/firecracker.sock
state: STARTED
pid: Optional[91798]
vmConfig: path: /home/ubuntu/vms/stage/vm-2/vm_config.json
memoryGb: 8
vcpu: 1
hostIface: Optional.empty
mac: Optional.empty


id: vm-1
home folder: /home/ubuntu/vms/stage/vm-1
socket: /home/ubuntu/vms/stage/vm-1/firecracker.sock
state: STARTED
pid: Optional[91804]
vmConfig: path: /home/ubuntu/vms/stage/vm-1/vm_config.json
memoryGb: 8
vcpu: 1
hostIface: Optional.empty
mac: Optional.empty
%
```

Start all VMs:
 
```bash
% fireguard startAll  
Starting VM with id vm-2...
Starting VM with id vm-1...
%
```

Restart VM:
 
```bash
% fireguard restart vm-1
Stopping VM with id vm-1...
Starting VM with id vm-1...
%
```