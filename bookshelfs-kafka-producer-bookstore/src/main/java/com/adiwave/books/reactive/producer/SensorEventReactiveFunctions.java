package com.adiwave.books.reactive.producer;

import com.adiwave.books.dto.SensorEventDto;
import com.adiwave.books.reactive.DlqEventUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.function.context.PollableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

@Configuration
@Profile("reactive")
public class SensorEventReactiveFunctions {

    private static final Logger log = LoggerFactory.getLogger(SensorEventReactiveFunctions.class);

    private static final String DLQ_CHANNEL = "sensorEventDlqProducer-out-0";
    

    private final DlqEventUtil dlqEventUtil;

    public SensorEventReactiveFunctions(DlqEventUtil dlqEventUtil) {
        this.dlqEventUtil = dlqEventUtil;
    }

    // to manage DLQ and retry, you can wrap the flow in a flatMap
    @Bean
    public Function<Flux<SensorEventDto>, Mono<Void>> logEventReceivedSaveInDBEventReceived() {
        return fluxEvent -> fluxEvent
                .flatMap(this::consumeMessage)
                .then();
    }

    private Mono<Void> consumeMessage(SensorEventDto message) {
        return logEventReceived().apply(Flux.just(message))
                .then()
                .retry(2)
                .onErrorResume(throwable -> dlqEventUtil.handleDlq(message, throwable, DLQ_CHANNEL));
    }

    
    public Function<Flux<SensorEventDto>, Flux<SensorEventDto>> logEventReceived() {
        return fluxEvent -> fluxEvent
                .doOnNext(sensorEvent -> log.info("Message received: {}", sensorEvent));
    }
    
    // code example to also save a record in local DB table:
    // 1.Save a copy to local db
    // public Function<Flux<SensorEventDto>, Mono<Void>> saveInDBEventReceived() {
    //    return fluxEvent -> fluxEvent
    //            .flatMap(sensorEvent -> sensorEventDao.save(Mono.just(sensorEvent)))
    //            .then();
    //}


    @PollableBean
    public Supplier<Flux<SensorEventDto>> sensorEventProducer() {
        final RandomGenerator random = RandomGenerator.getDefault();
        return () -> Flux.just(new SensorEventDto(UUID.randomUUID(), Instant.now(), random.nextDouble(1.0, 31.0)));
    }

}
