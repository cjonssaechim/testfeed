package me.saechimdaeki.testfeed.common.exception;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseEntity {
    private int status;
    private String name;
    private String code;
    private String message;


    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResponseEntity(
                        e.getHttpStatus().value(),
                        e.name(),
                        e.getCode(),
                        e.getMessage()));

    }
}