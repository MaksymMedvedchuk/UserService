package com.example.userservice.core.domain.dto;

import com.example.userservice.core.service.exception.ApiError;
import lombok.Data;

import java.util.List;

@Data
public class ApiErrorDto {

	private List<ApiError> errors;
}
