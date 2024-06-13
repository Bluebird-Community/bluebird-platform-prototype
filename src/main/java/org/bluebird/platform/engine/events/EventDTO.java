package org.bluebird.platform.engine.events;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import org.bluebird.platform.engine.payloads.Payload;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name="events")
@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO<T extends Payload> {
    @Id
    private UUID id;
    private String namespace;
    private String ref; // reference to external system, e.g. id or something
    private String uei; // TODO MVR find another naming for this
    private String source; // the source of event (e.g. a system, software, etc.)
    private LocalDateTime creationTime;
    private Integer level;

    private String consolidationKey; // Key to consolidate on

    @ElementCollection
    private Map<String, String> properties = new HashMap<>();

//    private Integer payloadVersion; // version > 0
//    private String payloadType; // The type of the payload
    @Transient // TODO MVR jsonp
    private T payload; // Optional

    public EventRef asRef() {
        return new EventRef(namespace, ref);
    }

}
