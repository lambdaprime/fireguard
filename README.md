fireguard - Firecracker MicroVMs management application

lambdaprime <id.blackmesa@gmail.com>

# Documentation

* [Quick Start](/docs/QuickStart.md)
* [How to create new origin image](/docs/CreateNewImage.md)

# Requirements

- Java 11
- Screen version 4.06.02 (GNU) 23-Oct-17
- firecracker v0.20.0
- jq-1.5-1-a5b5cbe
- isc-dhcp-server 4.4.1
* iproute2

Requires NOPASSWD sudo access to:

* ip
* iptables
* isc-dhcp-server

# Download

You can download **fireguard** from <https://github.com/lambdaprime/fireguard/tree/master/fireguard/release>

# Configuration

Before using **fireguard** make sure to create configuration file .fireguard and place it in your $HOME location:

```
originVm = <ORIGIN_VM_LOCATION>
hostIface = <HOST_IFACE>
```

Where:

ORIGIN\_VM\_LOCATION -- directory with original VM. It should be self sufficient and include everything what is needed to run a VM (kernel, vm_config.json, etc). See Quick Start for more details.

HOST\_IFACE -- host OS external network interface name (so VMs can access Internet)

# Usage

```bash
fireguard [ --config CONFIG_FILE ]  <COMMAND>
```

CONFIG_FILE is a path to **fireguard** config file (default is ~/.fireguard)
COMMAND is one of the following: vm, net

## vm

vm command accepts following arguments:

- create [JQ\_EXPRESSION] -- create a new VM by copying ORIGIN to a STAGE. If JQ_EXPRESSION is given then update its vm\_config.json with accordance to it.
- showAll -- show information about all available VMs
- startAll -- start all VMs
- stopAll -- stop all VMs (right now it is done by killing the process)
- restart VM\_ID -- restart VM with given id
- start VM\_ID -- start a VM with given id
- stop VM\_ID -- stop a VM with given id

Where:

JQ\_EXPRESSION -- it is a jq expression which will be sent to jq together with vm\_config.json. It allows you to change certain VM configuration parameters.

## net

net command accepts following arguments:

- create SUBNET NETMASK -- create a new network with given SUBNET and NETMASK. You cannot have two networks with same SUBNET.
- attach VM\_ID NETWORK\_ID -- attach virtual machine VM\_ID to the network NETWORK\_ID. You need to restart the VM after so it will join the network.

# Examples

## Configuration file

```
originVm = /home/ubuntu/vms/alpinelinux-3.8-kernel4.14
hostIface = enp0s3
```

## Usage

Create new VM:

```bash 
% fireguard vm create 
Creating new VM...
id: vm-1
home folder: /home/ubuntu/fireguardHome/stage/vm-1
socket: /home/ubuntu/fireguardHome/stage/vm-1/firecracker.sock
state: STOPPED
pid: Optional.empty
vmConfig: path: /home/ubuntu/fireguardHome/stage/vm-1/vm_config.json
memoryGb: 1000
vcpu: 1
hostIface: Optional.empty
mac: Optional.empty
%
```

Start a VM:

```bash
% fireguard vm start vm-1
Starting VM with id vm-1...
%
```

Show all available VMs:

```bash
% fireguard vm showAll
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
% fireguard vm startAll
Starting VM with id vm-1...

Starting VM with id vm-2...

%
```

Restart VM:
 
```bash
% fireguard vm restart vm-1
Stopping VM with id vm-1...
Starting VM with id vm-1...
%
```

Create network
 
```bash
% fireguard net create 10.1.2.0 255.255.255.0
Creating new network...
id: net-1
subnet: /10.1.2.0
netmask: /255.255.255.0
ifaces: []
%
```

Attach vm-1 to network net-1

```bash
% fireguard net attach vm-1 net-1            
Attaching vm-1 to net-1 network...
```
