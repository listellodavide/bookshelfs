package com.adiwave.books.reactive.handler;

import com.adiwave.books.imperative.controller.ProduceSensorEventBridge;
import com.adiwave.books.message.SensorEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@Profile("reactive")
public class SensorEventHandler {

    private final ProduceSensorEventBridge producerSensorEvent;

    @Autowired
    public SensorEventHandler(ProduceSensorEventBridge producerSensorEvent) {
        this.producerSensorEvent = producerSensorEvent;
    }

    public Mono<ServerResponse> pushSensorEventMessage(ServerRequest request) {
        return request.bodyToMono(SensorEvent.class)
                .map(producerSensorEvent::publishMessage)
                .flatMap(success -> ServerResponse.created(URI.create(request.path()))
                        .bodyValue(success));
    }
}
