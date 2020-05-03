This document explains how you can use **fireguard** on example of helloVM which is VM with Alpine Linux.

# Create an origin VM

Fireguard allows you to create a new VMs based on origin VM. So let's first create it:

Create origin VM folder and cd into it:

```bash
mkdir helloVM
cd helloVM/
```

Download the kernel for a VM:

```bash
curl -fsSL -o hello-vmlinux.bin https://github.com/lambdaprime/fireguard/raw/master/origin/alpinelinux-3.8-kernel4.14/hello-vmlinux.bin
```

Download storage with Alpine Linux:

```bash
curl -fsSL -o hello-rootfs.ext4 https://github.com/lambdaprime/fireguard/raw/master/origin/alpinelinux-3.8-kernel4.14/hello-rootfs.ext4
```

# Create a vm_config.json

```bash
cat > vm_config.json <<EOF
{
    "logger": {
        "log_fifo": "/dev/null",
        "metrics_fifo": "/dev/null"
    },
    "boot-source": {
        "kernel_image_path": "hello-vmlinux.bin",
        "boot_args": "console=ttyS0 reboot=k panic=1 pci=off ipv6.disable=1"
    },
    "drives": [
        {
            "drive_id": "rootfs",
            "path_on_host": "hello-rootfs.ext4",
            "is_root_device": true,
            "is_read_only": false
        }
    ],
    "machine-config": {
        "vcpu_count": 1,
        "mem_size_mib": 1000,
        "ht_enabled": true
    },
    "actions": {
        "action_type": "InstanceStart"
    }
}
EOF
```

Test that origin VM works (credentials root/root):

```bash
rm /tmp/firecracker.sock; firecracker --api-sock /tmp/firecracker.sock --config-file vm_config.json
```

# Configure fireguard

```bash
cat > ~/.fireguard <<EOF
originVm = <HELLOVM_ORIGIN>
hostIface = <HOST_IFACE>
EOF
```

Where:

HELLOVM_ORIGIN -- path to the helloVM folder where origin VM is located (was created earlier)

HOST\_IFACE -- host OS external network interface name (so VMs can access Internet)

# Manage multiple VMs

Create VMs:

```bash
% fireguard vm create
Creating new VM...
id: vm-1
home folder: ***/stage/vm-1
socket: ***/stage/vm-1/firecracker.sock
state: STOPPED
pid: Optional.empty
vmConfig: path: ***/stage/vm-1/vm_config.json
memoryGb: 1
vcpu: 1
hostIface: Optional.empty
mac: Optional.empty

% fireguard vm create
Creating new VM...
id: vm-2
home folder: ***/stage/vm-2
socket: ***/stage/vm-2/firecracker.sock
state: STOPPED
pid: Optional.empty
vmConfig: path: ***/stage/vm-2/vm_config.json
memoryGb: 1
vcpu: 1
hostIface: Optional.empty
mac: Optional.empty
```

Start all VMs:

```bash
% fireguard vm startAll 
Starting VM with id vm-1...

Starting VM with id vm-2...

```

Connect to VM:

```bash
% screen -ls
There are screens on:
	3945.vm-1	(01/24/20 22:03:16)	(Detached)
	3942.vm-2	(01/24/20 22:03:16)	(Detached)
% screen -r vm-1
```

# Network setup

This covers network setup under Ubuntu.

First disable ufw:

```bash
ufw disable
systemctl stop ufw
reboot
```

Then create a new network:

```bash
% fireguard net create 10.1.2.0 255.255.255.0
Creating new network...
id: net-1
subnet: /10.1.2.0
netmask: /255.255.255.0
ifaces: []
%
```

Attach VMs to it:

```bash
% fireguard net attach vm-1 net-1
Attaching vm-1 to net-1 network...

% fireguard net attach vm-2 net-1
Attaching vm-2 to net-1 network...
```

Restart the VMs:

```bash
% fireguard vm stopAll
Stopping VM with id vm-1...

Stopping VM with id vm-2...

% fireguard vm startAll
Starting VM with id vm-1...

Starting VM with id vm-2...
```

Then go inside of the VM and start networking

```bash
localhost:~# /etc/init.d/networking start
 * Starting networking ...
 *   eth0 ...
udhcpc: started, v1.28.4
udhcpc: sending discover
udhcpc: sending select for 10.1.2.2
udhcpc: lease of 10.1.2.2 obtained, lease time 43200
route: ioctl 0x890c failed: No such process                               [ ok ]
```

The VM will use dhcp client to talk to the **fireguard** managed dhcpd to obtain an IP address.

```bash
localhost:~# ping google.com
PING google.com (172.217.3.206): 56 data bytes
64 bytes from 172.217.3.206: seq=0 ttl=53 time=11.697 ms
64 bytes from 172.217.3.206: seq=1 ttl=53 time=13.777 ms
```