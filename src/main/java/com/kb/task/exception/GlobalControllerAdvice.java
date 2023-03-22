package com.kb.task.exception;

import com.kb.task.model.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> applicationHandler(ApplicationException e){
        log.error("Error occurs {}, {}", e.getLocalizedMessage(), e.getStackTrace());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new Response(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHancler(RuntimeException e){
        log.error("RuntimeError occurs! {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(new Response(ErrorCode.INTERNAL_SERVER_ERROR.name(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }
}
