package org.opennms.integration.xml.eventconf.events.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.NONE)
public class XmlEvent {

    @XmlElement(name = "mask")
    private XmlMask mask;

    @XmlElement(name = "uei", required = true)
    private String uei;

    @XmlElement(name = "event-label", required = true)
    private String eventLabel;

    @XmlElement(name = "descr", required = true)
    private String description;

    @XmlElement(name = "logmsg", required = true)
    private XmlLogmsg logmessage;

    @XmlElement(name = "severity", required = true)
    private String severity;

    @XmlElement(name = "operinstruct")
    private String operinstruct;

    @XmlElement(name = "alarm-data")
    private XmlAlarmData alarmData;

}
