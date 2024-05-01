package com.clearsolutions.userservice.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
	private Long id;

	@JsonProperty("email")
	@Column(unique = true)
	@Email
	private String email;

	@JsonProperty("firstName")
	@Column(name = "first_name")
	private String firstName;

	@JsonProperty("lastName")
	@Column(name = "last_name")
	private String lastName;

	@JsonProperty("birthday")
	private LocalDate birthday;

	@JsonProperty("address")
	private String address;

	@Column(name = "phone_Number")
	@JsonProperty("phoneNumber")
	private String phoneNumber;

	public User() {
	}

	public User(
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
