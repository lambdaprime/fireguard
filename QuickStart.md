This document explains how you can use fireguard on example of helloVM which is VM with Alpine Linux.

# Create an origin VM

Fireguard allows you to create a new VMs based on origin VM. So let's first create it:

Create origin VM folder and cd into it:

```bash
mkdir helloVM
cd helloVM/
```

Download the kernel for a VM:

```bash
curl -fsSL -o hello-vmlinux.bin https://s3.amazonaws.com/spec.ccfc.min/img/hello/kernel/hello-vmlinux.bin
```

Download storage with Alpine Linux:

```bash
curl -fsSL -o hello-rootfs.ext4 https://s3.amazonaws.com/spec.ccfc.min/img/hello/fsfiles/hello-rootfs.ext4
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
store = <HELLOVMS>/store
originVm = <HELLOVM_ORIGIN>
stage = <HELLOVMS>/stage
firecracker = <FIRECRACKER>
EOF
```

Where:

HELLOVMS -- root folder for all managed helloVMs

HELLOVM_ORIGIN -- path to the helloVM folder where origin VM is located (was created earlier)

FIRECRACKER -- fullpath to firecracker executable

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
Starting VM with id vm-2...
Starting VM with id vm-1...
```

Connect to VM:

```bash
% screen -ls
There are screens on:
	3945.vm-1	(01/24/20 22:03:16)	(Detached)
	3942.vm-2	(01/24/20 22:03:16)	(Detached)
% screen -r vm-1
```