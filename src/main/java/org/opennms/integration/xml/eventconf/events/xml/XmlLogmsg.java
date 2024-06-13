package org.opennms.integration.xml.eventconf.events.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;
import lombok.Data;

@Data
@XmlRootElement(name = "logmsg")
@XmlAccessorType(XmlAccessType.NONE)
public class XmlLogmsg {
    @XmlAttribute(name = "dest")
    private XmlLogDestType dest;

    @XmlValue
    private String content;
}
