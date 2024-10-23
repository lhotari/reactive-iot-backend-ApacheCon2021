package com.github.lhotari.reactiveiotbackend;

import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.reactive.client.api.EndOfStreamAction;
import org.apache.pulsar.reactive.client.api.ReactiveMessageReader;
import org.apache.pulsar.reactive.client.api.ReactivePulsarClient;
import org.apache.pulsar.reactive.client.api.StartAtSpec;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class EventFireHoseController {

    private final ReactiveMessageReader<TelemetryEvent> reactiveMessageReader;

    public EventFireHoseController(ReactivePulsarClient reactivePulsarClient) {
        reactiveMessageReader = reactivePulsarClient
            .messageReader(Schema.JSON(TelemetryEvent.class))
            .topic("telemetry_ingest")
            .startAtSpec(StartAtSpec.ofEarliest())
            .endOfStreamAction(EndOfStreamAction.POLL)
            .build();
    }

    @GetMapping("/firehose")
    public Flux<ServerSentEvent<TelemetryEvent>> firehose() {
        return reactiveMessageReader
            .readMany()
            .map(message -> ServerSentEvent.builder(message.getValue()).id(message.getMessageId().toString()).build());
    }
}
