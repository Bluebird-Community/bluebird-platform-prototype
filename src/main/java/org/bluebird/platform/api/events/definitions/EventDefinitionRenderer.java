package org.bluebird.platform.api.events.definitions;

import lombok.AllArgsConstructor;
import org.bluebird.platform.api.events.definitions.parser.EventDefinitionParser;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

@AllArgsConstructor
public class EventDefinitionRenderer {
    private final EventRenderContext context;

    public String render(String input) {
        if (input == null) return null;
        if (context == null) throw new NullPointerException("Context must not be null");
        final var tokens = new EventDefinitionParser().parse(input);
        return tokens.stream().map(token -> substitude(context, token))
                .map(it -> it == null ? "" : it)
                .collect(Collectors.joining(""));
    }

    private String substitude(EventRenderContext context, Token token) {
        final var event = context.getEvent();
        return switch (token.getType()) {
            case EMPTY -> "";
            case CONSTANT -> token.getToken();
            case EVENT_UEI -> event.getUei();
            case EVENT_LEVEL -> Integer.toString(event.getLevel());
            case EVENT_DB_ID -> event.getRef();
            case EVENT_SOURCE -> event.getSource();
            case EVENT_DESCR -> (String) event.getProperties().get("description"); // TODO MVR
            case EVENT_LOGMSG_CONTENT -> (String) event.getProperties().get("message"); // TODO MVR
            case EVENT_CONSOLIDATION_KEY -> event.getConsolidationKey();
            default -> "TODO";
            case EVENT_NODE_ID -> {
                yield "TODO";
//                if (event.getNodeCriteria() != null) {
//                    yield event.getNodeCriteria().getId().toString();
//                }
//                yield "undefined";
            }
//            case EVENT_TIME -> event.getTime().toString();
//            case EVENT_SHORT_TIME -> event.getTime().toString(); // TODO MVR no longer supported
//            case EVENT_HOST -> "TODO_MVR"; // TODO MVR
//            case EVENT_INTERFACE -> "TODO_MVR"; // TODO MVR
//            case EVENT_IFINDEX -> "TODO_MVR"; // TODO MVR
//            case EVENT_INTERFACE_ADDRESS -> event.getIpAddress();
//            case EVENT_PRIMARY_INTERFACE -> "TODO MVR"; // TODO MVR
//            case EVENT_SNMP_HOST -> "TODO MVR";
//            case EVENT_SERVICE -> "TODO MVR";
//            case EVENT_SNMP -> "TODO MVR";
//            case EVENT_SNMP_ID -> event.getSnmpInfo().getId();
//            case EVENT_SNMP_TRAP_OID -> event.getSnmpInfo().getTrap_oid();
//            case EVENT_SNMP_IDTEXT -> "TODO MVR";
//            case EVENT_SNMP_VERSION -> event.getSnmpInfo().getVersion();
//            case EVENT_SNMP_SPECIFIC -> event.getSnmpInfo().getSpecific().toString();
//            case EVENT_SNMP_GENERIC -> event.getSnmpInfo().getGeneric().toString();
//            case EVENT_SNMP_COMMUNITY -> event.getSnmpInfo().getCommunity();
//            case EVENT_SEVERITY -> "TODO MVR";
//            case EVENT_OPERINSTR -> "TODO MVR";
//            case EVENT_MOUSEOVERTEXT -> "TODO MVR";
//            case EVENT_TTICKET_ID -> "TODO MVR";
//            case EVENT_NUM_PARMS -> "" + event.getParameters().size();
//            case EVENT_NUM_PREFIX -> "TODO MVR";
//            case EVENT_NAME_NUMBERED_PREFIX -> "TODO MVR";
//            case EVENT_PARM ->
//                    event.getParameters().stream().filter(it -> it.getName().equalsIgnoreCase(token.getToken())).findFirst()
//                            .map(EventLogParameterEntity::getValue) // TODO MVR add consideration of ENCODING etc.
//                            .orElseThrow();
//            case EVENT_HARDWARE -> "TODO MVR";
//            case EVENT_ASSET -> "TODO MVR";
//            case NODE_LABEL -> event.getNodeCriteria().getNodeLabel();
//            case NODE_LOCATION -> event.getNodeCriteria().getLocation();
//            case NODE_FOREIGN_SOURCE -> event.getNodeCriteria().getForeignSource();
//            case NODE_FOREIGN_ID -> event.getNodeCriteria().getForeignId();
//            case NODE_IFALIAS -> "TODO MVR";
        };
    }

}



