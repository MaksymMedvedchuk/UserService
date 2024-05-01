package com.clearsolutions.userservice.core.domain.dto;

import com.clearsolutions.userservice.core.service.exception.ApiError;
import lombok.Data;

import java.util.List;

@Data
public class ApiErrorDto {

	private List<ApiError> errors;
}
