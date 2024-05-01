package com.example.userservice.core.domain.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

	@Valid
	private UserDto data;

	private List<Link> links;

	public ResponseDto(final UserDto data) {
		this.data = data;
	}
}
