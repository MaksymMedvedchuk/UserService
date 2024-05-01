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

	@JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "id")
	private Long id;

	@JsonProperty("email")
	private String email;

	@JsonProperty("birthday")
	private LocalDate birthday;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("address")
	private String address;

	@JsonProperty("phoneNumber")
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

