package me.saechimdaeki.testfeed.common.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import me.saechimdaeki.testfeed.common.exception.BaseException;
import me.saechimdaeki.testfeed.common.exception.ErrorResponseEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->
			errors.put(error.getField(), error.getDefaultMessage())
		);
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponseEntity> handle(BaseException e) {
		return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
	}

}
