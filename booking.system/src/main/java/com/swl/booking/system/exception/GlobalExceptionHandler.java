package com.swl.booking.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolationException;
import com.swl.booking.system.response.ApiResponse;
import com.swl.booking.system.util.CommonConstant;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		StringBuilder errorMessage = new StringBuilder("Validation failed: ");
		ex.getBindingResult().getFieldErrors().forEach(error -> errorMessage.append(error.getField()).append(" ")
				.append(error.getDefaultMessage()).append("; "));
		return new ApiResponse<>(CommonConstant.MSG_PREFIX_FAILED, errorMessage.toString());
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse<Void> handleBindException(BindException ex) {
		StringBuilder errorMessage = new StringBuilder("Validation failed: ");
		ex.getBindingResult().getFieldErrors().forEach(error -> errorMessage.append(error.getField()).append(" ")
				.append(error.getDefaultMessage()).append("; "));
		return new ApiResponse<>(CommonConstant.MSG_PREFIX_FAILED, errorMessage.toString());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse<Void> handleConstraintViolationException(ConstraintViolationException ex) {
		StringBuilder errorMessage = new StringBuilder("Validation failed: ");
		ex.getConstraintViolations().forEach(violation -> errorMessage.append(violation.getPropertyPath()).append(" ")
				.append(violation.getMessage()).append("; "));
		return new ApiResponse<>(CommonConstant.MSG_PREFIX_FAILED, errorMessage.toString());
	}   
	
	@ExceptionHandler(AlreadyExitException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidInputException(AlreadyExitException ex) {
        ApiResponse<Void> response = new ApiResponse<>(CommonConstant.MSG_PREFIX_FAILED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
