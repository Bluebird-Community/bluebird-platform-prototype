package org.opennms.integration.xml.eventconf.events.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * The Mask for event configuration: The mask contains one
 * or more 'maskelements' which uniquely identify an event.
 */
@Data
@XmlRootElement(name = "mask")
@XmlAccessorType(XmlAccessType.NONE)
public class XmlMask {

    @XmlElement(name = "maskelement", required = true)
    private List<XmlMaskElement> maskElements = new ArrayList<>();

    @XmlElement(name = "varbind")
    private List<XmlVarbind> varbinds = new ArrayList<>();

}