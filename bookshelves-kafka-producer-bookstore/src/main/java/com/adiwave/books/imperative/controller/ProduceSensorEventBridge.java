package com.adiwave.books.imperative.controller;

import com.adiwave.books.avro.SensorEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class ProduceSensorEventBridge {

    private final StreamBridge streamBridge;

    @Autowired
    public ProduceSensorEventBridge(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }


    public boolean publishMessage(SensorEvent message) {
        return streamBridge.send("sensorEventProducer-out-0", message);
    }
}
