package com.github.lhotari.reactiveiotbackend;

import com.github.lhotari.reactive.pulsar.adapter.MessageSpec;
import com.github.lhotari.reactive.pulsar.adapter.ReactiveMessageSender;
import com.github.lhotari.reactive.pulsar.adapter.ReactivePulsarClient;
import com.github.lhotari.reactive.pulsar.resourceadapter.ReactiveProducerCache;
import org.apache.pulsar.client.api.Schema;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class IngestController {
    private final ReactiveMessageSender<TelemetryEvent> reactiveMessageSender;

    public IngestController(ReactivePulsarClient reactivePulsarClient,
                            ReactiveProducerCache reactiveProducerCache) {
        reactiveMessageSender = reactivePulsarClient
                .messageSender(Schema.JSON(TelemetryEvent.class))
                .topic("telemetry_ingest")
                .cache(reactiveProducerCache)
                .maxInflight(100)
                .build();
    }

    @PostMapping("/telemetry")
    public Mono<Void> ingestTelemetry(@RequestBody Flux<TelemetryEvent> telemetryEventFlux) {
        return reactiveMessageSender
                .sendMessages(telemetryEventFlux.map(MessageSpec::of))
                .then();
    }
}
