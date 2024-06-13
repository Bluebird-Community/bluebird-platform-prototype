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
@XmlRootElement(name = "varbind")
@XmlAccessorType(XmlAccessType.NONE)
public class XmlVarbind {

    @XmlAttribute(name = "textual-convention")
    private String textualConvention;

    @XmlElement(name = "vbnumber", required = true)
    private Integer vbnumber;

    @XmlElement(name = "vbvalue", required = true)
    private List<String> values = new ArrayList<>();

}