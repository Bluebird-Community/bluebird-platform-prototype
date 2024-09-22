package org.bluebird.platform.integrations.opennms.alarmd;

import org.bluebird.platform.integration.opennms.protobuf.model.OpennmsModelProtos;

public class OpennmsEventBuilder {

    // TODO MVR this should be easier or different, as this does not fully support the OpenNMS EVent API, but this is good enough for now
    private final OpennmsModelProtos.Event.Builder builder;

    private OpennmsEventBuilder() {
        this.builder = OpennmsModelProtos.Event.newBuilder();
    }

    public static OpennmsEventBuilder newNodeDownBuilder(String source, OpennmsNodeRef nodeRef) {
        return new OpennmsEventBuilder()
                .withSeverity(OpennmsModelProtos.Severity.MAJOR)
                .withSource(source)
                .withUei("uei.opennms.org/nodes/nodeDown")
                .withNodeCriteria(nodeRef.toCriteria());
    }

    public OpennmsEventBuilder withSeverity(OpennmsModelProtos.Severity originalSeverity) {
        this.builder.setSeverity(originalSeverity);
        return this;
    }

    public OpennmsEventBuilder withSource(String source) {
        this.builder.setSource(source);
        return this;
    }

    public OpennmsEventBuilder withUei(String uei) {
        this.builder.setUei(uei);
        return this;
    }

    public OpennmsEventBuilder withNodeCriteria(OpennmsModelProtos.NodeCriteria nodeCriteria) {
        this.builder.setNodeCriteria(nodeCriteria);
        return this;
    }

    public OpennmsEventBuilder withLogDest(String logndisplay) {
        // TODO MVR this is probably busted and not working properly
        this.builder.setDisplay(logndisplay.contains("display"));
        this.builder.setLog(logndisplay.contains("log"));
        return this;
    }

    public OpennmsEventBuilder withLogMessage(String message) {
        this.builder.setLogMessage(message);
        return this;
    }

    public OpennmsModelProtos.Event build() {
        return this.builder.build();
    }
}
