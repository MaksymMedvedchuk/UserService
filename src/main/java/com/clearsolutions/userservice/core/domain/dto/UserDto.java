package com.clearsolutions.userservice.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	@Email(message = "Incorrectly formed email address")
	private String email;

	@NotBlank(message = "Can't be empty")
	private String firstName;

	@NotBlank(message = "Can't be empty")
	private String lastName;

	@Past(message = "Should be in past")
	private LocalDate birthday;

	private String address;

	private String phoneNumber;

	public UserDto(
		final String email,
		final String firstName,
		final String lastName,
		final LocalDate birthday,
		final String address,
		final String phoneNumber
	) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}
}
