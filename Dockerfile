# docker build . -t fireguard
# docker run --privileged --device=/dev/kvm  -it fireguard

FROM ubuntu:focal

RUN apt -y update && apt -y install \
  less \
  vim \
  curl \
  wget \
  unzip

# installing fileguard prereqs
RUN apt -y update && apt -y install \
  sudo \
  openjdk-17-jdk-headless \
  jq \
  iproute2 \
  screen \
  iptables \
  isc-dhcp-server \
  syslog-ng

# create "ubuntu" user
RUN useradd -s /bin/bash -m ubuntu
RUN echo 'ubuntu:ubuntu' | chpasswd

# setup firecracker
RUN addgroup --gid 108 kvm
RUN usermod -a -G kvm ubuntu
RUN wget https://github.com/firecracker-microvm/firecracker/releases/download/v0.20.0/firecracker-v0.20.0-x86_64 -O /usr/bin/firecracker
RUN chmod a+x /usr/bin/firecracker

# install fileguard
ARG HOMEDIR=/home/ubuntu
COPY fireguard/release/fireguard-1.zip $HOMEDIR
RUN unzip $HOMEDIR/fireguard*zip -d $HOMEDIR
RUN echo 'export PATH='$HOMEDIR'/fireguard:$PATH' >> $HOMEDIR/.bashrc

# configure env for fireguard
RUN usermod -a -G sudo ubuntu
RUN usermod -a -G dhcpd ubuntu
RUN echo "%sudo   ALL=(ALL:ALL) ALL" >> /etc/sudoers
RUN echo "ubuntu    ALL = NOPASSWD: /usr/bin/ip" >> /etc/sudoers
RUN echo "ubuntu    ALL = NOPASSWD: /usr/sbin/iptables" >> /etc/sudoers
RUN chown root:dhcpd /etc/dhcp/dhcpd.conf
RUN chmod 664 /etc/dhcp/dhcpd.conf
RUN echo 'INTERFACESv4="tap1"' > /etc/default/isc-dhcp-server

# configure fireguard
COPY origin $HOMEDIR/originVm
RUN echo 'originVm = '$HOMEDIR'/originVm/alpinelinux-3.8-kernel4.14' >> $HOMEDIR/.fireguard
RUN echo '# External network interface name (so VMs can access Internet)' >> $HOMEDIR/.fireguard
RUN echo '# This property left empty and need to be populated manually' >> $HOMEDIR/.fireguard
RUN echo '# based on "ip a" command' >> $HOMEDIR/.fireguard
RUN echo '#hostIface =' >> $HOMEDIR/.fireguard

RUN echo 'echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!"' >> $HOMEDIR/.bashrc
RUN echo 'echo "- Make sure to run: sudo sh -c \"echo 1 > /proc/sys/net/ipv4/conf/all/proxy_arp\""' >> $HOMEDIR/.bashrc
RUN echo 'echo "- Update '$HOMEDIR'/.fireguard (see instructions inside)"' >> $HOMEDIR/.bashrc
RUN echo 'echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n"' >> $HOMEDIR/.bashrc

RUN chown -R ubuntu:ubuntu $HOMEDIR/.*
USER ubuntu
ENV HOME $HOMEDIR
WORKDIR $HOMEDIR

