package com.example.userservice.core.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class AppErrorDto<T> {

	private List<T> errors;
}
