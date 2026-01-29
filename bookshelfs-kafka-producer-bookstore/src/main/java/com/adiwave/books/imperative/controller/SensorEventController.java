package com.adiwave.books.imperative.controller;

import com.adiwave.books.dto.SensorEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import com.adiwave.books.avro.SensorEvent;

@RestController
@RequestMapping("/api/v0/sensor-event")
@Profile("!reactive")
public class SensorEventController {

    private final ProduceSensorEventBridge producerSensorEvent;

    @Autowired
    public SensorEventController(ProduceSensorEventBridge producerSensorEvent) {
        this.producerSensorEvent = producerSensorEvent;
    }

    @PostMapping
    public Mono<ResponseEntity<Boolean>> pushSensorEventMessage(@RequestBody Mono<SensorEventDto> sensorEvent) {
        return sensorEvent
                .map(dto -> SensorEvent.newBuilder()
                        .setSensorId(dto.sensorId())
                        .setTimestampEvent(dto.timestampEvent())
                        .setDegree(dto.degree())
                        .build()
                )
                .map(producerSensorEvent::publishMessage)
                .map(ResponseEntity::ok);

    }

}
