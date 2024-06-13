package org.opennms.integration.xml.eventconf.events.xml;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlEnum
@XmlRootElement(name = "dest")
public enum XmlLogDestType {
    @XmlEnumValue("logndisplay")
    LOGNDISPLAY,
    @XmlEnumValue("displayonly")
    DISPLAYONLY,
    @XmlEnumValue("logonly")
    LOGONLY,
    @XmlEnumValue("suppress")
    SUPPRESS,
    @XmlEnumValue("donotpersist")
    DONOTPERSIST,
    @XmlEnumValue("discardtraps")
    DISCARDTRAPS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}