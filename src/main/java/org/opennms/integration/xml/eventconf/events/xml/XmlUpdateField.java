package org.opennms.integration.xml.eventconf.events.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "update-field")
@XmlAccessorType(XmlAccessType.NONE)
public class XmlUpdateField {

    @XmlAttribute(name = "field-name", required = true)
    private String fieldName;

    @XmlAttribute(name = "update-on-reduction")
    private Boolean updateOnReduction = Boolean.TRUE;

}
