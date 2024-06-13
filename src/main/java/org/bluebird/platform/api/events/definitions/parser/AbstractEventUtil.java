package org.bluebird.platform.api.events.definitions.parser;

import java.util.regex.Pattern;

public class AbstractEventUtil {
    /**
     * The Event ID xml
     */
    public static final String TAG_EVENT_DB_ID = "eventid";

    /**
     * The UEI xml tag
     */
    public static final String TAG_UEI = "uei";

    /**
     * The event source xml tag
     */
    public static final String TAG_SOURCE = "source";

    /**
     * The event descr xml tag
     */
    public static final String TAG_DESCR = "descr";

    /**
     * The event logmsg xml tag
     */
    public static final String TAG_LOGMSG = "logmsg";

    /**
     * The event time xml tag
     */
    public static final String TAG_TIME = "time";

    /**
     * The event time xml tag, short format
     */
    public static final String TAG_SHORT_TIME = "shorttime";

    /**
     * The event dpname xml tag
     */
    public static final String TAG_DPNAME = "dpname";

    /**
     * The event nodeid xml tag
     */
    public static final String TAG_NODEID = "nodeid";

    /**
     * The event nodelabel xml tag
     */
    public static final String TAG_NODELABEL = "nodelabel";

    /**
     * The event nodelocation xml tag
     */
    public static final String TAG_NODELOCATION = "nodelocation";

    /**
     * The event host xml tag
     */
    public static final String TAG_HOST = "host";

    /**
     * The event interface xml tag
     */
    public static final String TAG_INTERFACE = "interface";

    /**
     * The foreignsource for the event's nodeid xml tag
     */
    public static final String TAG_FOREIGNSOURCE = "foreignsource";

    /**
     * The foreignid for the event's nodeid xml tag
     */
    public static final String TAG_FOREIGNID = "foreignid";

    /**
     * The event ifindex xml tag
     */
    public static final String TAG_IFINDEX = "ifindex";

    /**
     * The reverse DNS lookup of the interface
     */
    public static final String TAG_INTERFACE_RESOLVE = "interfaceresolve";

    /**
     * The primary interface
     */
    public static final String TAG_PRIMARY_INTERFACE_ADDRESS = "primaryinterface";

    /**
     * The reverse DNS lookup of the interface
     */
    public static final String TAG_IFALIAS = "ifalias";

    /**
     * The event snmp id xml tag
     */
    public static final String TAG_SNMP_ID = "id";


    /**
     * The event snmp trapoid xml tag
     */
    public static final String TAG_SNMP_TRAP_OID = "trapoid";

    /**
     * The SNMP xml tag
     */
    public static final String TAG_SNMP = "snmp";

    /**
     * The event snmp idtext xml tag
     */
    public static final String TAG_SNMP_IDTEXT = "idtext";

    /**
     * The event snmp version xml tag
     */
    public static final String TAG_SNMP_VERSION = "version";

    /**
     * The event snmp specific xml tag
     */
    public static final String TAG_SNMP_SPECIFIC = "specific";

    /**
     * The event snmp generic xml tag
     */
    public static final String TAG_SNMP_GENERIC = "generic";

    /**
     * The event snmp community xml tag
     */
    public static final String TAG_SNMP_COMMUNITY = "community";

    /**
     * The event snmp host xml tag
     */
    public static final String TAG_SNMPHOST = "snmphost";

    /**
     * The event service xml tag
     */
    public static final String TAG_SERVICE = "service";

    /**
     * The event severity xml tag
     */
    public static final String TAG_SEVERITY = "severity";

    /**
     * The event operinstruct xml tag
     */
    public static final String TAG_OPERINSTR = "operinstruct";

    /**
     * The event mouseovertext xml tag
     */
    public static final String TAG_MOUSEOVERTEXT = "mouseovertext";

    public static final String TAG_TTICKET_ID = "tticketid";

    /**
     * The string that starts the expansion for an asset field - used to lookup values
     * of asset fields by their names
     */
    public static final String ASSET_BEGIN = "asset[";

    /**
     * The string that ends the expansion of a parm
     */
    public static final String ASSET_END_SUFFIX = "]";

    /**
     * The string that should be expanded to a list of all parm names
     */
    public static final String PARMS_NAMES = "parm[names-all]";

    /**
     * The string that should be expanded to a list of all parm values
     */
    public static final String PARMS_VALUES = "parm[values-all]";

    /**
     * The string that should be expanded to a list of all parms
     */
    public static final String PARMS_ALL = "parm[all]";

    /**
     * The string that starts the expansion for a parm - used to lookup values
     * of parameters by their names
     */
    public static final String PARM_BEGIN = "parm[";

    /**
     * Pattern used to match and parse 'parm' tokens.
     */
    public static final Pattern PARM_REGEX = Pattern.compile("^parm\\[(.*)\\]$");

    /**
     * The length of PARM_BEGIN
     */
    public static final int PARM_BEGIN_LENGTH = 5;

    /**
     * The string that should be expanded to the number of parms
     */
    public static final String NUM_PARMS_STR = "parm[##]";

    /**
     * The string that starts a parm number - used to lookup values of
     * parameters by their position
     */
    public static final String PARM_NUM_PREFIX = "parm[#";

    /**
     * The length of PARM_NUM_PREFIX
     */
    public static final int PARM_NUM_PREFIX_LENGTH = 6;

    /**
     * The string that starts a request for the name of a numbered parm
     */
    public static final String PARM_NAME_NUMBERED_PREFIX = "parm[name-#";

    /**
     * The length of PARM_NAME_NUMBERED_PREFIX
     */
    public static final int PARM_NAME_NUMBERED_PREFIX_LENGTH = 11;

    /**
     * The string that ends the expansion of a parm
     */
    public static final String PARM_END_SUFFIX = "]";

    /**
     * For expansion of the '%parms[all]%' - the parm name and value are added
     * as delimiter separated list of ' <parmName>= <value>' strings
     */
    public static final char NAME_VAL_DELIM = '=';

    /**
     */
    public static final char SPACE_DELIM = ' ';

    /**
     * The values and the corresponding attributes of an element are added
     * delimited by ATTRIB_DELIM
     */
    public static final char ATTRIB_DELIM = ',';

    /**
     * Substitute the actual percent sign
     */
    public static final String TAG_PERCENT_SIGN = "pctsign";

    /**
     * The string that starts the expansion for a hardware field - used to lookup values
     * of hardware attributes by their index|name
     */
    public static final String HARDWARE_BEGIN = "hardware[";

    /**
     * The string that ends the expansion of a hardware
     */
    public static final String HARDWARE_END_SUFFIX = "]";
}
