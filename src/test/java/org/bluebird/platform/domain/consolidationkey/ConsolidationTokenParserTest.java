package org.bluebird.platform.domain.consolidationkey;

import org.assertj.core.api.Assertions;
import org.bluebird.platform.utils.Tuple;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

class ConsolidationTokenParserTest {

    private static final ConsolidationToken TOKEN_COLON = new ConsolidationToken(":", ConsolidationTokenType.CONSTANT);

    private static ConsolidationToken constant(String token) {
        return new ConsolidationToken(token, ConsolidationTokenType.CONSTANT);
    }

    private static ConsolidationToken param(String token) {
        return new ConsolidationToken(token, ConsolidationTokenType.PARAMETER);
    }

    @TestFactory
    Stream<DynamicTest> verifyTokenParsing() {
        return Stream.of(
                        Tuple.of(
                                "{uei}:{dpname}:{nodeid}:{interface}:{service}",
                                List.of(param("uei"), TOKEN_COLON, param("dpname"), TOKEN_COLON, param("nodeid"), TOKEN_COLON, param("interface"), TOKEN_COLON, param("service"))
                        ),
                        Tuple.of(
                                "uei.opennms.org/nodes/snmp/interfaceOperDown:{dpname}:{nodeid}:1234test",
                                List.of(constant("uei.opennms.org/nodes/snmp/interfaceOperDown:"), param("dpname"), TOKEN_COLON, param("nodeid"), constant(":1234test"))
                        )
                )
                .map(tuple -> {
                    final var input = tuple.first();
                    final var expectedTokens = tuple.second();
                    return DynamicTest.dynamicTest("Verify parsing of %s".formatted(input), () -> {
                        final var actualTokens = new ConsolidationTokenParser().parse(input);
                        Assertions.assertThat(actualTokens).containsExactlyElementsOf(expectedTokens);
                    });
                });
    }
}