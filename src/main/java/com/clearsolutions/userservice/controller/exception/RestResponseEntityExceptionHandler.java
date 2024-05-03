package com.clearsolutions.userservice.controller.exception;

import com.clearsolutions.userservice.config.ApiConstants;
import com.clearsolutions.userservice.core.domain.dto.ApiErrorDto;
import com.clearsolutions.userservice.core.service.exception.ApiError;
import com.clearsolutions.userservice.core.service.exception.DuplicateEmailException;
import com.clearsolutions.userservice.core.service.exception.MinorUserException;
import com.clearsolutions.userservice.core.service.exception.ResourceNotfoundException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		final MethodArgumentNotValidException ex,
		final HttpHeaders headers,
		final HttpStatusCode status,
		final WebRequest request
	) {
		final BindingResult bindingResult = ex.getBindingResult();
		final List<ApiError> errors = bindingResult.getFieldErrors().stream()
			.map(fieldError -> new ApiError(fieldError.getField(), fieldError.getDefaultMessage()))
			.collect(Collectors.toList());
		final ApiErrorDto apiErrorDto = createApaDtoError(errors);
		return new ResponseEntity<>(apiErrorDto, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(MinorUserException.class)
	public ResponseEntity<ApiErrorDto> handleMinorUserException(MinorUserException ex) {
		final ApiErrorDto apiErrorDto = createApaDtoError(ex.getError());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorDto);
	}

	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<ApiErrorDto> handleDuplicateEmailException(DuplicateEmailException ex) {
		final ApiErrorDto apiErrorDto = createApaDtoError(ex.getError());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorDto);
	}

	@ExceptionHandler(ResourceNotfoundException.class)
	public ResponseEntity<ApiErrorDto> handleDuplicateEmailException(ResourceNotfoundException ex) {
		final ApiErrorDto apiErrorDto = createApaDtoError(ex.getErrors());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorDto);
	}

	private ApiErrorDto createApaDtoError(final List<ApiError> ex) {
		final ApiErrorDto apiErrorDto = new ApiErrorDto();
		apiErrorDto.setErrors(ex);
		return apiErrorDto;
	}

	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<ApiErrorDto> handleJsonMappingException(JsonMappingException ex) {
		final String field = ex.getPath().toString().substring(ex.getPath().toString()
			.indexOf("\"") + 1, ex.getPath().toString().lastIndexOf("\""));
		final String massage = ex.getMessage();
		final ApiError error = new ApiError(field, massage);
		final List<ApiError> errors = new ArrayList<>();
		errors.add(error);
		final ApiErrorDto apiErrorDto = createApaDtoError(errors);
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(apiErrorDto);
	}

	@ExceptionHandler(PSQLException.class)
	public ResponseEntity<ApiErrorDto> handlePSQLException(PSQLException ex) {
		final String
			field =
			ex.getSQLState().equals(ApiConstants.SQL_STATE) ? ApiConstants.EMAIL : ApiConstants.CONSTRAINT_FIELD;
		final String message = ex.getMessage();
		final ApiErrorDto apiErrorDto = new ApiErrorDto();
		apiErrorDto.setErrors(Collections.singletonList(new ApiError(field, message)));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorDto);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiErrorDto> handleConstraintViolationException(ConstraintViolationException ex) {
		final List<ApiError> errors = ex
			.getConstraintViolations()
			.stream()
			.map(element -> new ApiError(element.getPropertyPath().toString(), element.getMessage()))
			.collect(Collectors.toList());
		final ApiErrorDto apiErrorDto = createApaDtoError(errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorDto);
	}
}
