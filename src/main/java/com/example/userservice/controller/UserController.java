package com.example.userservice.controller;

import com.example.userservice.core.converter.UserDataConverter;
import com.example.userservice.core.domain.dto.CustomPage;
import com.example.userservice.core.domain.dto.ResponseDto;
import com.example.userservice.core.domain.dto.UserDto;
import com.example.userservice.core.domain.dto.UserPatchDto;
import com.example.userservice.core.domain.entity.User;
import com.example.userservice.core.service.LinksProvider;
import com.example.userservice.core.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

	private final UserDataConverter converter;

	private final UserService userService;

	private final LinksProvider links;

	public UserController(
		final UserDataConverter converter,
		final UserService userService,
		final LinksProvider links
	) {
		this.converter = converter;
		this.userService = userService;
		this.links = links;
	}

	@PostMapping("")
	@Operation(summary = "Create user")
	public ResponseEntity<ResponseDto> create(@RequestBody final @Valid UserDto userDto) {
		final User user = converter.convertToEntityAttribute(userDto);
		final User savedUser = userService.save(user);
		final UserDto result = converter.convertToDatabaseColumn(savedUser);
		log.info("Created new user with email: [{}]", savedUser.getEmail());
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(new ResponseDto(result, links.addLinksToCreate(result)));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete user")
	public ResponseEntity<String> delete(@PathVariable final Long id) {
		userService.delete(id);
		log.info("Successfully deleted user with id: [{}]", id);
		return ResponseEntity.status(HttpStatus.OK).body("User was successfully deleted");
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update one/some user fields")
	//if parameter is passed as null, field will not change
	public ResponseEntity<ResponseDto> patch(
		@RequestBody final UserPatchDto patchDto,
		@PathVariable final Long id
	) {
		final User updatedUser = userService.patch(patchDto, id);
		final UserDto result = converter.convertToDatabaseColumn(updatedUser);
		log.info("Updated user with id: [{}]", id);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new ResponseDto(result, links.addLinksToPatch(result)));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update all user fields")
	public ResponseEntity<ResponseDto> update(@RequestBody @Valid final UserDto userDto, @PathVariable final Long id) {
		final User user = converter.convertToEntityAttribute(userDto);
		user.setId(id);
		final User updatedUser = userService.update(user);
		final UserDto result = converter.convertToDatabaseColumn(updatedUser);
		log.info("Updated user with id: [{}]", id);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new ResponseDto(result, links.addLinksToUpdate(userDto)));
	}

	@GetMapping("")
	@Operation(summary = "Search for users by birth date range")
	public ResponseEntity<CustomPage<ResponseDto>> getByBirthdayRange(
		@RequestParam final LocalDate startDate,
		@RequestParam final LocalDate endDate,
		@RequestParam(defaultValue = "1", name = "page") final Integer page,
		@RequestParam(defaultValue = "25", name = "limit") final Integer limit
	) {
		final Page<User> list = userService.getByBirthdayRange(startDate, endDate, page, limit);
		final CustomPage<ResponseDto> result = converter.convertToPage(list);
		log.info("Got list of users between dates : [{}], [{}]", startDate, endDate);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}
