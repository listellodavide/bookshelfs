package com.adiwave.books.reactive.consumer;

import com.adiwave.books.message.SensorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Profile("reactive")
@Configuration
public class SensorEventReactiveFunctions {

    private static final Logger log = LoggerFactory.getLogger(SensorEventReactiveFunctions.class);

    private static final String DLQ_CHANNEL = "sensorEventDlqConsumer-dlq-0";

    private final DlqEventUtil dlqEventUtil;

    @Autowired
    public SensorEventReactiveFunctions(DlqEventUtil dlqEventUtil) {
        this.dlqEventUtil = dlqEventUtil;
    }

    @Bean
    public Function<Flux<SensorEvent>, Mono<Void>> consumeMessageChain() {
        return fluxEvent -> fluxEvent
                .flatMap(this::consumeMessage)
                .then();
    }

    private Mono<Void> consumeMessage(SensorEvent message) {
        return logEventReceived().apply(Flux.just(message))
                .then()
                .retry(2)
                .onErrorResume(throwable -> dlqEventUtil.handleDlq(message, throwable, DLQ_CHANNEL));
    }


    public Function<Flux<SensorEvent>, Flux<SensorEvent>> logEventReceived() {
        return fluxEvent -> fluxEvent
                .doOnNext(sensorEvent -> log.info("Message received: {}", sensorEvent));
    }

}
