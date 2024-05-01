package com.example.userservice.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDtoField {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "id")
	private Long id;

	@JsonProperty("email")
	private String email;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("address")
	private String address;

	@Column(name = "phoneNumber")
	private String phoneNumber;
}

