package com.kb.task.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;

}
