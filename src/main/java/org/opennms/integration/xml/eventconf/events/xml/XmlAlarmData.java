package org.opennms.integration.xml.eventconf.events.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = "alarm-data")
@XmlAccessorType(XmlAccessType.NONE)
public class XmlAlarmData {

    @XmlAttribute(name = "reduction-key", required = true)
    private String reductionKey;

    @XmlAttribute(name = "alarm-type")
    private Integer alarmType;

    @XmlAttribute(name = "clear-key")
    private String clearKey;

    @XmlAttribute(name = "auto-clean")
    private Boolean autoClean;

    @XmlElement(name = "update-field")
    private List<XmlUpdateField> updateFields = new ArrayList<>();

}
