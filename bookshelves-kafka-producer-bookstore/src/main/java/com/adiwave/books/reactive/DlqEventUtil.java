package com.adiwave.books.reactive;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* For Spring Cloud Stream Reactive, DLQ and retry props doesn't work.
   You need to do custom management
 */
@Component
@Profile("reactive")
public class DlqEventUtil {

    private static final Logger log = LoggerFactory.getLogger(DlqEventUtil.class);

    private final StreamBridge streamBridge;

    public DlqEventUtil(StreamBridge streamBridge){
        this.streamBridge = streamBridge;
    }

    public  <T> Mono<Void> handleDlq(T message, Throwable throwable, String channel) {
        log.error("Error for message: {}", message, throwable);
        streamBridge.send(channel, message);
        return Mono.empty();
    }
}
