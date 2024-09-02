package org.bluebird.integrations.opennms.events.substitution;

import java.util.Objects;
import java.util.function.Predicate;

public enum OpennmsTokenType {
    EVENT_UEI(OpennmsEventUtils.TAG_UEI::equals),
    EVENT_DB_ID(OpennmsEventUtils.TAG_EVENT_DB_ID::equals),
    EVENT_SOURCE(OpennmsEventUtils.TAG_SOURCE::equals),
    EVENT_DESCR(OpennmsEventUtils.TAG_DESCR::equals),
    EVENT_LOGMSG_CONTENT(OpennmsEventUtils.TAG_LOGMSG::equals),
    EVENT_NODE_ID(OpennmsEventUtils.TAG_NODEID::equals),
    EVENT_TIME(OpennmsEventUtils.TAG_TIME::equals),
    EVENT_SHORT_TIME(OpennmsEventUtils.TAG_SHORT_TIME::equals),
    EVENT_HOST(OpennmsEventUtils.TAG_HOST::equals),
    EVENT_INTERFACE(OpennmsEventUtils.TAG_INTERFACE::equals),
    EVENT_IFINDEX(OpennmsEventUtils.TAG_IFINDEX::equals),
    EVENT_INTERFACE_ADDRESS(OpennmsEventUtils.TAG_INTERFACE_RESOLVE::equals),
    EVENT_PRIMARY_INTERFACE(OpennmsEventUtils.TAG_PRIMARY_INTERFACE_ADDRESS::equals),
    EVENT_SNMP_HOST(OpennmsEventUtils.TAG_SNMPHOST::equals),
    EVENT_SERVICE(OpennmsEventUtils.TAG_SERVICE::equals),
    EVENT_SNMP(OpennmsEventUtils.TAG_SNMP::equals),
    EVENT_SNMP_ID(OpennmsEventUtils.TAG_SNMP_ID::equals),
    EVENT_SNMP_TRAP_OID(OpennmsEventUtils.TAG_SNMP_TRAP_OID::equals),
    EVENT_SNMP_IDTEXT(OpennmsEventUtils.TAG_SNMP_IDTEXT::equals),
    EVENT_SNMP_VERSION(OpennmsEventUtils.TAG_SNMP_VERSION::equals),
    EVENT_SNMP_SPECIFIC(OpennmsEventUtils.TAG_SNMP_SPECIFIC::equals),
    EVENT_SNMP_GENERIC(OpennmsEventUtils.TAG_SNMP_GENERIC::equals),
    EVENT_SNMP_COMMUNITY(OpennmsEventUtils.TAG_SNMP_COMMUNITY::equals),
    EVENT_SEVERITY(OpennmsEventUtils.TAG_SEVERITY::equals),
    EVENT_OPERINSTR(OpennmsEventUtils.TAG_OPERINSTR::equals),
    EVENT_MOUSEOVERTEXT(OpennmsEventUtils.TAG_MOUSEOVERTEXT::equals),
    EVENT_TTICKET_ID(OpennmsEventUtils.TAG_TTICKET_ID::equals),
    EVENT_NUM_PARMS(OpennmsEventUtils.NUM_PARMS_STR::equals),
    EVENT_NUM_PREFIX(OpennmsEventUtils.PARM_NUM_PREFIX::equals),
    EVENT_NAME_NUMBERED_PREFIX(token -> token.startsWith(OpennmsEventUtils.PARM_NAME_NUMBERED_PREFIX)),
    EVENT_PARM(token -> OpennmsEventUtils.PARM_REGEX.matcher(token).matches()),
    EVENT_HARDWARE(token -> token.startsWith(OpennmsEventUtils.HARDWARE_BEGIN)),
    EVENT_ASSET(token -> token.startsWith(OpennmsEventUtils.ASSET_BEGIN)),
    NODE_LABEL(OpennmsEventUtils.TAG_NODELABEL::equals),
    NODE_LOCATION(OpennmsEventUtils.TAG_NODELOCATION::equals),
    NODE_FOREIGN_SOURCE(OpennmsEventUtils.TAG_FOREIGNSOURCE::equals),
    NODE_FOREIGN_ID(OpennmsEventUtils.TAG_FOREIGNID::equals),
    NODE_IFALIAS(OpennmsEventUtils.TAG_IFALIAS::equals),
    EVENT_LEVEL(it -> it.equalsIgnoreCase("level")),
    EVENT_CONSOLIDATION_KEY(it -> it.equalsIgnoreCase("key")),
    EMPTY(OpennmsEventUtils.TAG_DPNAME::equals),
    CONSTANT((token) -> true),
    ;

    private final Predicate<String> tokenMatcher;

    OpennmsTokenType(Predicate<String> tokenMatcher) {
        this.tokenMatcher = Objects.requireNonNull(tokenMatcher);
    }

    public boolean matches(String token) {
        return tokenMatcher.test(token);
    }
}
