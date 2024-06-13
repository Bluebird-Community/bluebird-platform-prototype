package org.bluebird.platform.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
@With
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventLogRef {
    public static EventLogRef of(String source, Object id) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(id);
        return new EventLogRef(source, id.toString());
    }

    @Column(name = "ref_source")
    private String source;

    @Column(name = "ref_id")
    private String id;

    public boolean matches(String regexp) {
        return Pattern.compile(regexp).matcher(toString()).matches();
    }

    @Override
    public String toString() {
        return String.join(":", source, id);
    }
}
