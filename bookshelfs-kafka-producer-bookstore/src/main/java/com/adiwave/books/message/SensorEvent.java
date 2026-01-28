package com.adiwave.books.message;

import java.time.Instant;

public record SensorEvent(
        String sensorId,
        Instant timestampEvent,
        Double degree
) {
}
