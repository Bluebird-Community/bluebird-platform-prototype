package org.bluebird.integrations.opennms.events.substitution;

import jakarta.transaction.Transactional;
import org.bluebird.integrations.opennms.events.OpennmsEventDefinitionParser;
import org.bluebird.integrations.opennms.persistence.OpennmsEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OpennmsEventTemplateRenderer {

    @Autowired
    private OpennmsEventRepository eventRepository;

    @Transactional
    public String render(OpennmsEventRenderContext context, String template) {
        if (template == null) return null;
        if (context == null) throw new NullPointerException("Context must not be null");
        final var tokens = new OpennmsEventDefinitionParser().parse(template);
        return tokens.stream().map(opennmsToken -> substitude(context, opennmsToken))
                .map(it -> it == null ? "" : it)
                .collect(Collectors.joining(""));
    }

    private String substitude(OpennmsEventRenderContext context, OpennmsToken opennmsToken) {
        final var event = context.event();
        // TODO MVR ensure this is not needed, as it is ugly
        final var opennmsEvent = eventRepository.getReferenceById(context.entity().getId());
        return switch (opennmsToken.type()) {
            case EMPTY -> "";
            case CONSTANT -> opennmsToken.token();
            case EVENT_UEI -> opennmsEvent.getUei();
            case EVENT_LEVEL -> Integer.toString(event.getLevel());
            case EVENT_DB_ID -> event.getRef();
            case EVENT_SOURCE -> event.getSource();
            case EVENT_DESCR -> opennmsEvent.getDescription();
            case EVENT_LOGMSG_CONTENT -> opennmsEvent.getEventlogmsg();
            case EVENT_CONSOLIDATION_KEY -> event.getConsolidationKey();
            case EVENT_NODE_ID -> {
                if (opennmsEvent.getNode() != null) {
                    yield opennmsEvent.getNode().getId().toString();
                }
                yield null;
            }
//            case EVENT_TIME -> opennmsEvent.getTime().toString();
//            case EVENT_SHORT_TIME -> opennmsEvent.getTime().toString(); // TODO MVR no longer supported
//            case EVENT_HOST -> "TODO_MVR"; // TODO MVR
            case EVENT_INTERFACE -> opennmsEvent.getIpAddress();
            case EVENT_IFINDEX -> opennmsEvent.getIfIndex();
            case EVENT_INTERFACE_ADDRESS -> opennmsEvent.getIpAddress();
//            case EVENT_PRIMARY_INTERFACE -> "TODO MVR"; // TODO MVR
//            case EVENT_SNMP_HOST -> "TODO MVR";
            case EVENT_SERVICE -> opennmsEvent.getService().getName();
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
//                    opennmsEvent.getParameters().stream().filter(it -> it.getName().equalsIgnoreCase(token.getToken())).findFirst()
//                            .map(EventLogParameterEntity::getValue) // TODO MVR add consideration of ENCODING etc.
//                            .orElseThrow();
//            case EVENT_HARDWARE -> "TODO MVR";
//            case EVENT_ASSET -> "TODO MVR";
            case NODE_LABEL -> {
                if (opennmsEvent.getNode() != null) {
                    yield opennmsEvent.getNode().getLabel();
                }
                yield null;
            }
            case NODE_LOCATION -> {
                if (opennmsEvent.getNode() != null) {
                    yield opennmsEvent.getNode().getLocation();
                }
                yield null;
            }
            case NODE_FOREIGN_SOURCE -> {
                if (opennmsEvent.getNode() != null) {
                    yield opennmsEvent.getNode().getForeignSource();
                }
                yield null;
            }
            case NODE_FOREIGN_ID -> {
                if (opennmsEvent.getNode() != null) {
                    yield opennmsEvent.getNode().getForeignId();
                }
                yield null;
            }
//            case NODE_IFALIAS -> "TODO MVR";
            default -> "TODO_MVR";
        };
    }

}



