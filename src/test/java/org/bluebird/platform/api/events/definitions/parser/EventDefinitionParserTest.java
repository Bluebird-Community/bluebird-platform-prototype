package org.bluebird.platform.api.events.definitions.parser;

import org.assertj.core.api.Assertions;
import org.bluebird.platform.api.events.definitions.Token;
import org.bluebird.platform.api.events.definitions.TokenType;
import org.bluebird.platform.util.Tuple;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

class EventDefinitionParserTest {

    private static final Token TOKEN_COLON = new Token(":", TokenType.CONSTANT);
    private static final Token TOKEN_SOURCE = new Token(AbstractEventUtil.TAG_SOURCE, TokenType.EVENT_SOURCE);
    private static final Token TOKEN_SNMP_HOST = new Token(AbstractEventUtil.TAG_SNMPHOST, TokenType.EVENT_SNMP_HOST);
    private static final Token TOKEN_DB_ID = new Token(AbstractEventUtil.TAG_SNMP_ID, TokenType.EVENT_SNMP_ID);
    private static final Token TOKEN_SNMP_GENERIC = new Token(AbstractEventUtil.TAG_SNMP_GENERIC, TokenType.EVENT_SNMP_GENERIC);
    private static final Token TOKEN_SNMP_SPECIFIC = new Token(AbstractEventUtil.TAG_SNMP_SPECIFIC, TokenType.EVENT_SNMP_SPECIFIC);
    private static final Token TOKEN_UEI = new Token(AbstractEventUtil.TAG_UEI, TokenType.EVENT_UEI);
    private static final Token TOKEN_EMPTY = new Token(AbstractEventUtil.TAG_DPNAME, TokenType.EMPTY);
    private static final Token TOKEN_NODE_ID = new Token(AbstractEventUtil.TAG_NODEID, TokenType.EVENT_NODE_ID);
    private static final Token TOKEN_INTERFACE = new Token(AbstractEventUtil.TAG_INTERFACE, TokenType.EVENT_INTERFACE);
    private static final Token TOKEN_SERVICE = new Token(AbstractEventUtil.TAG_SERVICE, TokenType.EVENT_SERVICE);

    private static Token param(String param) {
        var token = String.join("", AbstractEventUtil.PARM_BEGIN, param, AbstractEventUtil.PARM_END_SUFFIX);
        return new Token(token, TokenType.EVENT_PARM);
    }

    private static Token constant(String token) {
        return new Token(token, TokenType.CONSTANT);
    }

    @TestFactory
    Stream<DynamicTest> verifyTokenParsing() {
        return Stream.of(
                        Tuple.of(
                                "%source%:%snmphost%:%id%:%generic%:%specific%",
                                List.of(TOKEN_SOURCE, TOKEN_COLON, TOKEN_SNMP_HOST, TOKEN_COLON, TOKEN_DB_ID, TOKEN_COLON, TOKEN_SNMP_GENERIC, TOKEN_COLON, TOKEN_SNMP_SPECIFIC)
                        ),
                        Tuple.of(
                                "%uei%:%dpname%:%nodeid%:%interface%:%service%",
                                List.of(TOKEN_UEI, TOKEN_COLON, TOKEN_EMPTY, TOKEN_COLON, TOKEN_NODE_ID, TOKEN_COLON, TOKEN_INTERFACE, TOKEN_COLON, TOKEN_SERVICE)
                        ),
                        Tuple.of(
                                "%uei%:%dpname%:%nodeid%:%interface%:%service%:%parm[resource.name]%:%parm[alertDef.name]%",
                                List.of(TOKEN_UEI, TOKEN_COLON, TOKEN_EMPTY, TOKEN_COLON, TOKEN_NODE_ID, TOKEN_COLON, TOKEN_INTERFACE, TOKEN_COLON, TOKEN_SERVICE, TOKEN_COLON, param("resource.name"), TOKEN_COLON, param("alertDef.name"))
                        ),
                        Tuple.of(
                                "%uei%:%parm[perspective]%:%nodeid%:%interface%:%service%",
                                List.of(TOKEN_UEI, TOKEN_COLON, param("perspective"), TOKEN_COLON, TOKEN_NODE_ID, TOKEN_COLON, TOKEN_INTERFACE, TOKEN_COLON, TOKEN_SERVICE)
                        ),
                        Tuple.of(
                                "uei.opennms.org/nodes/snmp/interfaceOperDown:%dpname%:%nodeid%:%parm[snmpifindex]%",
                                List.of(constant("uei.opennms.org/nodes/snmp/interfaceOperDown:"), TOKEN_EMPTY, TOKEN_COLON, TOKEN_NODE_ID, TOKEN_COLON, param("snmpifindex"))
                        ),
                        Tuple.of(
                                "uei.opennms.org/nodes/snmp/interfaceOperDown:%dpname%:%nodeid%:%parm[snmpifindex]",
                                List.of(constant("uei.opennms.org/nodes/snmp/interfaceOperDown:"), TOKEN_EMPTY, TOKEN_COLON, TOKEN_NODE_ID, constant(":%parm[snmpifindex]"))
                        )
                )
                .map(tuple -> {
                    final var input = tuple.first();
                    final var expectedTokens = tuple.second();
                    return DynamicTest.dynamicTest("Verify parsing of %s".formatted(input), () -> {
                        final var actualTokens = new EventDefinitionParser().parse(input);
                        Assertions.assertThat(actualTokens).containsExactlyElementsOf(expectedTokens);
                    });
                });
    }
}