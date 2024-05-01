package com.clearsolutions.userservice.core.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MinorUserException extends RuntimeException {

	private List<ApiError> error;
}
