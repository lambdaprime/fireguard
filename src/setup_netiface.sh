#!/bin/bash

# To cleanup:
# ip tuntap del tap1 mode tap

if [[ $EUID -ne 0 ]]; then
   echo "This script must be run as root" 
   exit 1
fi

if [ "$#" -ne 3 ]; then
    echo "Usage: $(basename $0) <MAC> <ID> <HOST_IFACE>"
    exit 1
fi

MAC=$1
ID=$2
HOST_IFACE=$3

MICROVM_IFACE=tap${ID}

ip tuntap add $MICROVM_IFACE mode tap
ip addr add 172.16.${ID}.1/24 dev $MICROVM_IFACE
ip link set $MICROVM_IFACE up
iptables -t nat -A POSTROUTING -o $HOST_IFACE -j MASQUERADE
iptables -A FORWARD -m conntrack --ctstate RELATED,ESTABLISHED -j ACCEPT
iptables -A FORWARD -i $MICROVM_IFACE -o $HOST_IFACE -j ACCEPT
ufw reload


cat >> /etc/dhcp/dhcpd.conf <<EOF
subnet 172.16.${ID}.0 netmask 255.255.255.0 {
group{
host vm${ID} {
     hardware ethernet ${MAC};
     fixed-address 172.16.${ID}.100;
     option routers                  172.16.${ID}.1;
     option subnet-mask              255.255.255.0;
     option domain-name-servers      8.8.4.4, 8.8.8.8;
}

}}
EOF

/etc/init.d/isc-dhcp-server restart
