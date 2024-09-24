package org.bluebird.platform.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "0_symbiont_events")
@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    @Id
    private UUID id; // internal id
    private String namespace;
    private String ref; // reference to external system, e.g. id or something
    private String source; // the source of event (e.g. a system, software, etc.)
    private LocalDateTime creationTime;
    private String consolidationKey; // Key to consolidate on

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "headers", columnDefinition = "jsonb")
    private Map<String, String> headers = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "jsonb")
    private Map<String, Object> payload = new HashMap<>();

    // TODO MVR this does not make sense anymore?!
    @Transient
    public EventRef asRef() {
        return new EventRef(namespace, ref);
    }

    public Map<String, Object> asMap() {
        final Map<String, Object> map = new HashMap<>(headers);
        map.put("namespace", namespace);
        if (ref != null) {
            map.put("ref", ref);
        }
        if (source != null) {
            map.put("source", source);
        }
        if (consolidationKey != null) {
            map.put("consolidationKey", consolidationKey);
        }
        if (payload != null) {
            map.put("payload", payload);
        }
        return map;
    }
}
