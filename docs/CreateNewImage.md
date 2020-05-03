This document explains how to create a new Firecracker image based on Ubuntu for **fireguard**.

Create ubuntu folder for new origin image and cd in it

```bash
mkdir ubuntu
cd ubuntu
```

Create empty 1GB image

```bash
dd if=/dev/zero of=./ubuntu.ext4 bs=1MB count=1000
mkfs.ext4 ./ubuntu.ext4
```

Mount it

```bash
sudo mount ./ubuntu.ext4 /media
```

Download Ubuntu qemu image (it has compiled kernel with KVM support)

```bash
wget http://cloud-images.ubuntu.com/minimal/releases/eoan/release/ubuntu-19.10-minimal-cloudimg-amd64.img
```

Convert to raw

```bash
qemu-img convert -p -O raw ubuntu-19.10-minimal-cloudimg-amd64.img ubuntu-19.10-minimal-cloudimg-amd64.raw
```

Mount raw volume image

```bash
losetup /dev/loop0 ubuntu-19.10-minimal-cloudimg-amd64.raw
sudo kpartx -a /dev/loop0
sudo mount /dev/mapper/loop0p1 /mnt
```

Copy all data from volume image to ext image

```bash
sudo cp -rfp /mnt/* /media/cdrom
```

Uncompress the kernel

```bash
wget https://raw.githubusercontent.com/torvalds/linux/master/scripts/extract-vmlinux
chmod u+x extract-vmlinux
sudo ./extract-vmlinux /mnt/boot/vmlinuz-5.3.0-1016-kvm > vmlinux-5.3.0-1016-kvm
```

Unmount volume image

```bash
sudo umount /mnt
sudo kpartx -d /dev/loop0
losetup -d /dev/loop0
```

Unmount ext image

```bash
sudo umount /media/cdrom
```

Create vm_config.json

```bash
cat > vm_config.json <<EOF
{
    "logger": {
        "log_fifo": "/dev/null",
        "metrics_fifo": "/dev/null"
    },
    "boot-source": {
        "kernel_image_path": "vmlinux-5.3.0-1016-kvm",
        "boot_args": "console=ttyS0 reboot=k panic=1 pci=off ipv6.disable=1 raid=noautodetect"
    },
    "drives": [
        {
            "drive_id": "rootfs",
            "path_on_host": "ubuntu.ext4",
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

Then run it

```bash
rm /tmp/firecracker.sock; firecracker --api-sock /tmp/firecracker.sock --config-file vm_config.json
```

Once you are ready clean up the files:

```bash
rm extract-vmlinux *img *ra
```

Your Ubuntu origin image is ready, now you can configure **fireguard** to use it.

# How to reset password

You need to update vm_config.json and specify following kernel argument:

```
init=/bin/bash
```

Then start the VM. Once there, reset root password and add new user ubuntu

```bash
passwd
adduser ubuntu
adduser ubuntu sudo
```

# How to setup network in the VM

```bash
cat > /etc/netplan/01-netcfg.yaml << EOF
network:
    ethernets:
        eth0:
            dhcp4: true
            optional: true
EOF
```
