package com.example.userservice.core.domain.dto;

import lombok.Data;

@Data
public class DataDto<UserDto> {

	private UserDto userDto;
}
