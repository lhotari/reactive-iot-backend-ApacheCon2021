package com.github.lhotari.reactiveiotbackend;

import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.reactive.client.api.MessageSpec;
import org.apache.pulsar.reactive.client.api.ReactiveMessageSender;
import org.apache.pulsar.reactive.client.api.ReactiveMessageSenderCache;
import org.apache.pulsar.reactive.client.api.ReactivePulsarClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class IngestController {

    private final ReactiveMessageSender<TelemetryEvent> reactiveMessageSender;

    public IngestController(
        ReactivePulsarClient reactivePulsarClient,
        ReactiveMessageSenderCache reactiveMessageSenderCache
    ) {
        reactiveMessageSender =
            reactivePulsarClient
                .messageSender(Schema.JSON(TelemetryEvent.class))
                .topic("telemetry_ingest")
                .cache(reactiveMessageSenderCache)
                .maxInflight(100)
                .build();
    }

    @PostMapping("/telemetry")
    public Mono<Void> ingestTelemetry(@RequestBody Flux<TelemetryEvent> telemetryEventFlux) {
        return reactiveMessageSender.sendMany(telemetryEventFlux.map(MessageSpec::of)).then();
    }
}
