package com.adiwave.books.dto;

import java.time.Instant;
import java.util.UUID;

public record SensorEventDto(
        UUID sensorId,
        Instant timestampEvent,
        Double degree
) {
}
