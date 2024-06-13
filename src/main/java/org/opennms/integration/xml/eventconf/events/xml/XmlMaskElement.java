package org.opennms.integration.xml.eventconf.events.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = "maskelement")
@XmlAccessorType(XmlAccessType.NONE)
public class XmlMaskElement {

    @XmlElement(name = "mename", required = true)
    private String name;

    @XmlElement(name = "mevalue", required = true)
    private List<String> values = new ArrayList<>();
}
