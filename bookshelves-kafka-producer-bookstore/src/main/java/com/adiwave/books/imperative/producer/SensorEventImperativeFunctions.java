package com.adiwave.books.imperative.producer;

import com.adiwave.books.avro.SensorEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

@Configuration
@Profile("!reactive")
public class SensorEventImperativeFunctions {

    @Bean
    public Supplier<SensorEvent> sensorEventProducer() {
        RandomGenerator random = RandomGenerator.getDefault();

        // Called periodically by Spring Cloud Stream, generate the Avro generated class com.adiwave.books.avro.SensorEvent
        // based on application.yaml -> fixed-delay: 5000 # Sends a message every 5 seconds
        return () -> SensorEvent.newBuilder()
                .setSensorId(UUID.fromString("1FFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFF0"))
                .setTimestampEvent(Instant.now())
                .setDegree(random.nextDouble(1.0, 31.0))
                .build();
    }
}
