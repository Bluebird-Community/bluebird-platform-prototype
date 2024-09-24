package org.bluebird.integrations.github;

import lombok.AllArgsConstructor;
import org.bluebird.platform.domain.consolidationkey.ConsolidationKeyRenderer;
import org.bluebird.platform.domain.model.EventDTO;
import org.bluebird.platform.persistence.EventRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController()
@AllArgsConstructor
public class GithubActionHook {

    private final EventRepository eventRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ConsolidationKeyRenderer consolidationKeyRenderer;

    @PostMapping("/github-webhook")
    public void parse(@RequestHeader Map<String, String> headers, @RequestBody Map<String, Object> payload) {
        final var filteredHeaders = headers.keySet()
                .stream()
                .filter(it -> it.startsWith("x-"))
                .collect(Collectors.toMap(it -> it, headers::get));
        final var namespace = "github.com/payloads/%s".formatted(headers.get("x-github-event"));
        final var consolidationKeyTemplate = "{namespace}/{payload.repository.owner.login}/{payload.repository.name}/workflows/{payload.workflow.id}";
        final var event = new EventDTO()
                .withId(UUID.randomUUID()) // TODO MVR automatically assign id when persisting. must be fixed
                .withCreationTime(LocalDateTime.now())
                .withSource("github")
                .withNamespace(namespace)
                .withRef(headers.get("x-github-delivery"))
                .withHeaders(filteredHeaders)
                .withPayload(payload);
        final var consolidationKey = consolidationKeyRenderer.render(consolidationKeyTemplate, event);
        event.setConsolidationKey(consolidationKey);
        eventRepository.save(event);
        eventPublisher.publishEvent(event);
    }
}
