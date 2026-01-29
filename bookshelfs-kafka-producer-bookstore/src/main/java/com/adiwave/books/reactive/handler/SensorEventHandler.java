package com.adiwave.books.reactive.handler;

import com.adiwave.books.avro.SensorEvent;
import com.adiwave.books.dto.ResponseDto;
import com.adiwave.books.dto.SensorEventDto;
import com.adiwave.books.imperative.controller.ProduceSensorEventBridge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Component
@Profile("reactive")
public class SensorEventHandler {

    private final ProduceSensorEventBridge producerSensorEvent;

    @Autowired
    public SensorEventHandler(ProduceSensorEventBridge producerSensorEvent) {
        this.producerSensorEvent = producerSensorEvent;
    }

    public Mono<ServerResponse> pushSensorEventMessage(ServerRequest request) {
        return request.bodyToMono(SensorEventDto.class)
                .map(this::mapToAvro)
                .flatMap(avroRecord -> producerSensorEvent.publishMessage(avroRecord)
                        ? ServerResponse.ok().bodyValue(ResponseDto.createOKResponse("Message Sent to Kafka"))
                        : ServerResponse.status(500).bodyValue(ResponseDto.createErrorResponse( "Failed to send to Kafka", null, false))
                )
                .onErrorResume(e -> ServerResponse.status(500)
                        .bodyValue(ResponseDto.createErrorResponse("Kafka Serialization Failed", e, true)));

    }

    private SensorEvent mapToAvro(SensorEventDto dto) {
        return SensorEvent.newBuilder()
                .setSensorId(dto.sensorId())
                .setTimestampEvent(dto.timestampEvent())
                .setDegree(dto.degree())
                .build();
    }
}
