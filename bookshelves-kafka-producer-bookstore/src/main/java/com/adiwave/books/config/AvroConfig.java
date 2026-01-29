package com.adiwave.books.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AvroConfig {
    @PostConstruct
    public void setupAvro() {
        // This tells Avro how to handle java.util.UUID and java.time.Instant because of JSON Jackson serialize/deserializer issue
        // this otherwise will fail {"sensorId": "22222222-2222-2222-2222-222222222222", "timestampEvent": "2026-01-28T17:00:00Z", "degree": 97.32}
        org.apache.avro.specific.SpecificData.get().addLogicalTypeConversion(new org.apache.avro.Conversions.UUIDConversion());
        org.apache.avro.specific.SpecificData.get().addLogicalTypeConversion(new org.apache.avro.data.TimeConversions.TimestampMillisConversion());
    }
}
