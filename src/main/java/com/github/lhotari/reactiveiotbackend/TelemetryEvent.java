package com.github.lhotari.reactiveiotbackend;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TelemetryEvent {
    String n;
    double v;
}
