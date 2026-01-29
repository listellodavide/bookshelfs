package com.adiwave.books.reactive;

import com.adiwave.books.dto.SensorEventDto;
import com.adiwave.books.reactive.handler.SensorEventHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
@Profile("reactive")
public class RouterConfig {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/sensor-event",
                    method = RequestMethod.POST,
                    beanClass = SensorEventHandler.class,
                    beanMethod = "pushSensorEventMessage",
                    operation = @Operation(
                            operationId = "pushSensorEventMessage",
                            tags = "SensorEvents",
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = SensorEventDto.class))
                            ),
                            responses = @ApiResponse(
                                    responseCode = "201",
                                    content = @Content(schema = @Schema(implementation = Boolean.class))
                            )
                    )
            ),
    })
    public RouterFunction<ServerResponse> routes(
            SensorEventHandler sensorEventHandler) {
        return RouterFunctions.route(
                POST("/api/v1/sensor-event").and(accept(MediaType.APPLICATION_JSON)),
                sensorEventHandler::pushSensorEventMessage);

    }
}

