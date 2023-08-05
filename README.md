**fireguard** - Firecracker MicroVMs management application.

# Usage

```bash
fireguard [ --config CONFIG_FILE ]  <COMMAND>
```

CONFIG_FILE is a path to **fireguard** config file (default is ~/.fireguard)

COMMAND is one of the following: vm, net

## vm

vm command accepts following arguments:

- create [JQ_EXPRESSION] -- create a new VM by copying ORIGIN to a STAGE. If JQ_EXPRESSION is given then update its vm_config.json with accordance to it.
- showAll -- show information about all available VMs
- start VM_ID -- start a VM with given id. Once VM is started its console available through screen utility
- stop VM_ID -- stop a VM with given id
- startAll -- start all VMs
- stopAll -- stop all VMs (right now it is done by killing the process)
- restart VM_ID -- restart VM with given id

Where:

JQ_EXPRESSION -- it is a jq expression which will be sent to jq together with vm_config.json. It allows you to change certain VM configuration parameters.

## net

net command accepts following arguments:

- create SUBNET NETMASK -- create a new network with given SUBNET and NETMASK. You cannot have two networks with same SUBNET.
- attach VM_ID NETWORK_ID -- attach virtual machine VM_ID to the network NETWORK_ID. You need to restart the VM after so it will join the network.

# Documentation

[Documentation](http://portal2.atwebpages.com/fireguard)

# Download

[Release versions](fireguard/release/CHANGELOG.md)

lambdaprime <intid@protonmail.com>
