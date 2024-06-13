package org.bluebird.platform.api.events.definitions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {
    private final String token;
    private final TokenType type;
}
