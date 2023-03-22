package com.kb.task.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(HttpStatus.OK, "Success"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Please input the keyword"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")
    ;
    private HttpStatus status;
    private String message;
}
