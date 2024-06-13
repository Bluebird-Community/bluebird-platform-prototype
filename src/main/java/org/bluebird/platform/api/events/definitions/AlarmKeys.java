package org.bluebird.platform.api.events.definitions;

public record AlarmKeys(String reductionKey, String clearKey) {
    static public AlarmKeys of(String reductionKey, String clearKey) {
        return new AlarmKeys(reductionKey, clearKey);
    }
}
