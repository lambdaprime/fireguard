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
package id.jnix.net.dhcpd;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class DhcpdConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<InetAddress, SubnetSection> subnets = new LinkedHashMap<>();

    public DhcpdConfig() {}

    public DhcpdConfig(SubnetSection subnet) {
        this.subnets = Map.of(subnet.getSubnet(), subnet);
    }

    public void addSubnet(SubnetSection subnet) {
        subnets.put(subnet.getSubnet(), subnet);
    }

    public List<SubnetSection> getSubnets() {
        return List.copyOf(subnets.values());
    }

    public Optional<SubnetSection> getSubnet(InetAddress subnet) {
        return Optional.ofNullable(subnets.get(subnet));
    }

    @Override
    public String toString() {
        return subnets.values().stream()
                .map(SubnetSection::toString)
                .collect(Collectors.joining("\n"));
    }
}
