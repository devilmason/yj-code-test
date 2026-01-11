package com.wjc.codetest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice(value = {"com.wjc.codetest.product.controller"})
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> runTimeException(Exception e) {
        log.error("status :: {}, errorType :: {}, errorCause :: {}",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "runtimeException",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // 없는 프로덕트 조회시 exception 별도 처리
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException e) {
        log.error("status :: {}, errorType :: {}, errorCause :: {}",
                HttpStatus.NOT_FOUND,
                "productNotFoundException",
                e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // controller에서 넘기는 request 객체들 validation exception 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        log.error("status :: {}, errorType :: {}, errorCause :: {}",
                HttpStatus.BAD_REQUEST,
                "MethodArgumentNotValidException",
                errors);
        return ResponseEntity.badRequest().body(errors);
    }
}
