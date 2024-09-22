package org.bluebird.platform.integrations.opennms.alarmd;

import org.bluebird.platform.integration.opennms.protobuf.model.OpennmsModelProtos;

public record OpennmsNodeRef(String systemId, String nodeIdentifier) {
    public OpennmsNodeRef(String nodeIdentifier) {
        this("default", nodeIdentifier); // For test purposes // TODO MVR ...
    }

    public OpennmsModelProtos.NodeCriteria toCriteria() {
        if (nodeIdentifier.contains(":")) {
            return OpennmsModelProtos.NodeCriteria.newBuilder()
                    .setForeignSource(nodeIdentifier.split(":")[0])
                    .setForeignId(nodeIdentifier.split(":")[1])
                    .build();
        }
        return OpennmsModelProtos.NodeCriteria.newBuilder()
                .setId(Long.parseLong(nodeIdentifier))
                .build();
    }
}
