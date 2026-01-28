package com.adiwave.books.imperative.producer;

import com.adiwave.books.message.SensorEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

@Configuration
@Profile("!reactive")
public class SensorEventImperativeFunctions {

    @Bean
    public Supplier<SensorEvent> sensorEventProducer() {
        RandomGenerator random = RandomGenerator.getDefault();

        // Called periodically by Spring Cloud Stream
        // based on application.yaml -> fixed-delay: 5000 # Sends a message every 5 seconds
        return () -> new SensorEvent(
                "2",
                Instant.now(),
                random.nextDouble(1.0, 31.0)
        );
    }
}
