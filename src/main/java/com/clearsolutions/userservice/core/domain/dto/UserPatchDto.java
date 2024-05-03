package com.clearsolutions.userservice.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class UserPatchDto {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	private String email;

	private LocalDate birthday;

	private String firstName;

	private String lastName;

	private String address;

	private String phoneNumber;

	public UserPatchDto(
		final String email,
		final LocalDate birthday,
		final String firstName,
		final String lastName,
		final String address,
		final String phoneNumber
	) {
		this.email = email;
		this.birthday = birthday;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}
}

