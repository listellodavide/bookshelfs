package com.adiwave.books.reactive;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


/* For Spring Cloud Stream Reactive, DLQ and retry props doesn't work.
   You need to do custom management
 */
@Component
public class DlqEventUtil {

    private final StreamBridge streamBridge;

    public DlqEventUtil(StreamBridge streamBridge){
        this.streamBridge = streamBridge;
    }

    public  <T> Mono<Void> handleDLQ(T message, Throwable throwable, String channel) {
        log.error("Error for message: {}", message, throwable);
        streamBridge.send(channel, message);
        return Mono.empty();
    }
}
