package com.github.lhotari.reactiveiotbackend;

import com.github.lhotari.reactive.pulsar.adapter.EndOfStreamAction;
import com.github.lhotari.reactive.pulsar.adapter.ReactiveMessageReader;
import com.github.lhotari.reactive.pulsar.adapter.ReactivePulsarClient;
import com.github.lhotari.reactive.pulsar.adapter.StartAtSpec;
import org.apache.pulsar.client.api.Schema;
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
                .readMessages()
                .map(message -> ServerSentEvent.builder(message.getValue())
                        .id(message.getMessageId().toString())
                        .build());
    }
}
