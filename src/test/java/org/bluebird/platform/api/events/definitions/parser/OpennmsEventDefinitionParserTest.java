package org.bluebird.platform.api.events.definitions.parser;

import org.assertj.core.api.Assertions;
import org.bluebird.integrations.opennms.events.substitution.OpennmsEventUtils;
import org.bluebird.integrations.opennms.events.OpennmsEventDefinitionParser;
import org.bluebird.integrations.opennms.events.substitution.OpennmsToken;
import org.bluebird.integrations.opennms.events.substitution.OpennmsTokenType;
import org.bluebird.platform.utils.Tuple;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

class OpennmsEventDefinitionParserTest {

    private static final OpennmsToken OPENNMS_TOKEN_COLON = new OpennmsToken(":", OpennmsTokenType.CONSTANT);
    private static final OpennmsToken OPENNMS_TOKEN_SOURCE = new OpennmsToken(OpennmsEventUtils.TAG_SOURCE, OpennmsTokenType.EVENT_SOURCE);
    private static final OpennmsToken OPENNMS_TOKEN_SNMP_HOST = new OpennmsToken(OpennmsEventUtils.TAG_SNMPHOST, OpennmsTokenType.EVENT_SNMP_HOST);
    private static final OpennmsToken OPENNMS_TOKEN_DB_ID = new OpennmsToken(OpennmsEventUtils.TAG_SNMP_ID, OpennmsTokenType.EVENT_SNMP_ID);
    private static final OpennmsToken OPENNMS_TOKEN_SNMP_GENERIC = new OpennmsToken(OpennmsEventUtils.TAG_SNMP_GENERIC, OpennmsTokenType.EVENT_SNMP_GENERIC);
    private static final OpennmsToken OPENNMS_TOKEN_SNMP_SPECIFIC = new OpennmsToken(OpennmsEventUtils.TAG_SNMP_SPECIFIC, OpennmsTokenType.EVENT_SNMP_SPECIFIC);
    private static final OpennmsToken OPENNMS_TOKEN_UEI = new OpennmsToken(OpennmsEventUtils.TAG_UEI, OpennmsTokenType.EVENT_UEI);
    private static final OpennmsToken OPENNMS_TOKEN_EMPTY = new OpennmsToken(OpennmsEventUtils.TAG_DPNAME, OpennmsTokenType.EMPTY);
    private static final OpennmsToken OPENNMS_TOKEN_NODE_ID = new OpennmsToken(OpennmsEventUtils.TAG_NODEID, OpennmsTokenType.EVENT_NODE_ID);
    private static final OpennmsToken OPENNMS_TOKEN_INTERFACE = new OpennmsToken(OpennmsEventUtils.TAG_INTERFACE, OpennmsTokenType.EVENT_INTERFACE);
    private static final OpennmsToken OPENNMS_TOKEN_SERVICE = new OpennmsToken(OpennmsEventUtils.TAG_SERVICE, OpennmsTokenType.EVENT_SERVICE);

    private static OpennmsToken param(String param) {
        var token = String.join("", OpennmsEventUtils.PARM_BEGIN, param, OpennmsEventUtils.PARM_END_SUFFIX);
        return new OpennmsToken(token, OpennmsTokenType.EVENT_PARM);
    }

    private static OpennmsToken constant(String token) {
        return new OpennmsToken(token, OpennmsTokenType.CONSTANT);
    }

    @TestFactory
    Stream<DynamicTest> verifyTokenParsing() {
        return Stream.of(
                        Tuple.of(
                                "%source%:%snmphost%:%id%:%generic%:%specific%",
                                List.of(OPENNMS_TOKEN_SOURCE, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_SNMP_HOST, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_DB_ID, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_SNMP_GENERIC, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_SNMP_SPECIFIC)
                        ),
                        Tuple.of(
                                "%uei%:%dpname%:%nodeid%:%interface%:%service%",
                                List.of(OPENNMS_TOKEN_UEI, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_EMPTY, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_NODE_ID, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_INTERFACE, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_SERVICE)
                        ),
                        Tuple.of(
                                "%uei%:%dpname%:%nodeid%:%interface%:%service%:%parm[resource.name]%:%parm[alertDef.name]%",
                                List.of(OPENNMS_TOKEN_UEI, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_EMPTY, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_NODE_ID, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_INTERFACE, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_SERVICE, OPENNMS_TOKEN_COLON, param("resource.name"), OPENNMS_TOKEN_COLON, param("alertDef.name"))
                        ),
                        Tuple.of(
                                "%uei%:%parm[perspective]%:%nodeid%:%interface%:%service%",
                                List.of(OPENNMS_TOKEN_UEI, OPENNMS_TOKEN_COLON, param("perspective"), OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_NODE_ID, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_INTERFACE, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_SERVICE)
                        ),
                        Tuple.of(
                                "uei.opennms.org/nodes/snmp/interfaceOperDown:%dpname%:%nodeid%:%parm[snmpifindex]%",
                                List.of(constant("uei.opennms.org/nodes/snmp/interfaceOperDown:"), OPENNMS_TOKEN_EMPTY, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_NODE_ID, OPENNMS_TOKEN_COLON, param("snmpifindex"))
                        ),
                        Tuple.of(
                                "uei.opennms.org/nodes/snmp/interfaceOperDown:%dpname%:%nodeid%:%parm[snmpifindex]",
                                List.of(constant("uei.opennms.org/nodes/snmp/interfaceOperDown:"), OPENNMS_TOKEN_EMPTY, OPENNMS_TOKEN_COLON, OPENNMS_TOKEN_NODE_ID, constant(":%parm[snmpifindex]"))
                        )
                )
                .map(tuple -> {
                    final var input = tuple.first();
                    final var expectedTokens = tuple.second();
                    return DynamicTest.dynamicTest("Verify parsing of %s".formatted(input), () -> {
                        final var actualTokens = new OpennmsEventDefinitionParser().parse(input);
                        Assertions.assertThat(actualTokens).containsExactlyElementsOf(expectedTokens);
                    });
                });
    }
}