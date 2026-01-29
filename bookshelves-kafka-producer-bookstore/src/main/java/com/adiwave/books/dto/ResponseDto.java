package com.adiwave.books.dto;

import org.apache.commons.lang3.exception.ExceptionUtils;

public record ResponseDto(
        String status,
        String message,
        String stackTrace
) {
    public static ResponseDto createOKResponse(String message) {
        return new ResponseDto(
                "200 OK",
                message,
                ""
        );
    }

    public static ResponseDto createErrorResponse(String message, Throwable throwable, boolean includeStackTrace) {
        String trace = includeStackTrace ? ExceptionUtils.getStackTrace(throwable) : "Hidden for security";
        return new ResponseDto(
                "500 Server Error",
                message,
                trace
        );
    }
}
