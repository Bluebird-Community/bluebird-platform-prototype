package org.bluebird.platform.api.events.definitions;

import org.bluebird.platform.api.events.definitions.parser.AbstractEventUtil;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public enum TokenType {
    EVENT_UEI(AbstractEventUtil.TAG_UEI::equals),
    EVENT_DB_ID(AbstractEventUtil.TAG_EVENT_DB_ID::equals),
    EVENT_SOURCE(AbstractEventUtil.TAG_SOURCE::equals),
    EVENT_DESCR(AbstractEventUtil.TAG_DESCR::equals),
    EVENT_LOGMSG_CONTENT(AbstractEventUtil.TAG_LOGMSG::equals),
    EVENT_NODE_ID(AbstractEventUtil.TAG_NODEID::equals),
    EVENT_TIME(AbstractEventUtil.TAG_TIME::equals),
    EVENT_SHORT_TIME(AbstractEventUtil.TAG_SHORT_TIME::equals),
    EVENT_HOST(AbstractEventUtil.TAG_HOST::equals),
    EVENT_INTERFACE(AbstractEventUtil.TAG_INTERFACE::equals),
    EVENT_IFINDEX(AbstractEventUtil.TAG_IFINDEX::equals),
    EVENT_INTERFACE_ADDRESS(AbstractEventUtil.TAG_INTERFACE_RESOLVE::equals),
    EVENT_PRIMARY_INTERFACE(AbstractEventUtil.TAG_PRIMARY_INTERFACE_ADDRESS::equals),
    EVENT_SNMP_HOST(AbstractEventUtil.TAG_SNMPHOST::equals),
    EVENT_SERVICE(AbstractEventUtil.TAG_SERVICE::equals),
    EVENT_SNMP(AbstractEventUtil.TAG_SNMP::equals),
    EVENT_SNMP_ID(AbstractEventUtil.TAG_SNMP_ID::equals),
    EVENT_SNMP_TRAP_OID(AbstractEventUtil.TAG_SNMP_TRAP_OID::equals),
    EVENT_SNMP_IDTEXT(AbstractEventUtil.TAG_SNMP_IDTEXT::equals),
    EVENT_SNMP_VERSION(AbstractEventUtil.TAG_SNMP_VERSION::equals),
    EVENT_SNMP_SPECIFIC(AbstractEventUtil.TAG_SNMP_SPECIFIC::equals),
    EVENT_SNMP_GENERIC(AbstractEventUtil.TAG_SNMP_GENERIC::equals),
    EVENT_SNMP_COMMUNITY(AbstractEventUtil.TAG_SNMP_COMMUNITY::equals),
    EVENT_SEVERITY(AbstractEventUtil.TAG_SEVERITY::equals),
    EVENT_OPERINSTR(AbstractEventUtil.TAG_OPERINSTR::equals),
    EVENT_MOUSEOVERTEXT(AbstractEventUtil.TAG_MOUSEOVERTEXT::equals),
    EVENT_TTICKET_ID(AbstractEventUtil.TAG_TTICKET_ID::equals),
    EVENT_NUM_PARMS(AbstractEventUtil.NUM_PARMS_STR::equals),
    EVENT_NUM_PREFIX(AbstractEventUtil.PARM_NUM_PREFIX::equals),
    EVENT_NAME_NUMBERED_PREFIX(token -> token.startsWith(AbstractEventUtil.PARM_NAME_NUMBERED_PREFIX)),
    EVENT_PARM(token -> AbstractEventUtil.PARM_REGEX.matcher(token).matches()),
    EVENT_HARDWARE(token -> token.startsWith(AbstractEventUtil.HARDWARE_BEGIN)),
    EVENT_ASSET(token -> token.startsWith(AbstractEventUtil.ASSET_BEGIN)),
    NODE_LABEL(AbstractEventUtil.TAG_NODELABEL::equals),
    NODE_LOCATION(AbstractEventUtil.TAG_NODELOCATION::equals),
    NODE_FOREIGN_SOURCE(AbstractEventUtil.TAG_FOREIGNSOURCE::equals),
    NODE_FOREIGN_ID(AbstractEventUtil.TAG_FOREIGNID::equals),
    NODE_IFALIAS(AbstractEventUtil.TAG_IFALIAS::equals),
    EVENT_LEVEL(it -> it.equalsIgnoreCase("level")),
    EVENT_CONSOLIDATION_KEY(it -> it.equalsIgnoreCase("key")),
    EMPTY(AbstractEventUtil.TAG_DPNAME::equals),
    CONSTANT((token) -> true),
    ;

    private final Predicate<String> tokenMatcher;

    TokenType(Predicate<String> tokenMatcher) {
        this.tokenMatcher = Objects.requireNonNull(tokenMatcher);
    }

    public boolean matches(String token) {
        return tokenMatcher.test(token);
    }
}
