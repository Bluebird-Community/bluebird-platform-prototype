package org.opennms.integration.xml.eventconf.events.xml;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "events")
public class XmlEvents {

    @XmlElement(name = "event")
    private List<XmlEvent> events = new ArrayList<>();

}

