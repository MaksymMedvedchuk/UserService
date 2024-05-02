package com.clearsolutions.userservice.core.service.impl;

import com.clearsolutions.userservice.config.ApiConstants;
import com.clearsolutions.userservice.core.domain.dto.UserPatchDto;
import com.clearsolutions.userservice.core.domain.entity.User;
import com.clearsolutions.userservice.core.repository.UserRepository;
import com.clearsolutions.userservice.core.service.UserService;
import com.clearsolutions.userservice.core.service.exception.ApiError;
import com.clearsolutions.userservice.core.service.exception.DuplicateEmailException;
import com.clearsolutions.userservice.core.service.exception.MinorUserException;
import com.clearsolutions.userservice.core.service.exception.ResourceNotfoundException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final ObjectMapper objectMapper;

	@Value("${min.user.age}")
	private Long minAge;

	private UserServiceImpl(final UserRepository userRepository, final ObjectMapper objectMapper) {
		this.userRepository = userRepository;
		this.objectMapper = objectMapper;
	}

	@Override
	public User save(final User user) {
		checkDuplicationEmail(user);
		if (calculateAge(user) < minAge) {
			log.error("Attempted to save minor user with email: [{}]", user.getEmail());
			throw new MinorUserException(List.of(new ApiError(ApiConstants.BIRTHDAY, ApiConstants.AGE_LESS_18)));
		}
		log.info("Saving user with email: [{}]", user.getEmail());
		return userRepository.save(user);
	}

	@Override
	public void delete(final Long id) {
		final User user = findUserByIdOrThrow(id);
		log.info("Deleting user with email: [{}]", user.getEmail());
		userRepository.delete(user);
	}

	@Override
	public User findUserByIdOrThrow(final Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> {
				log.error("User with id [{}] not found", id);
				return new ResourceNotfoundException(List.of(new ApiError(
					ApiConstants.USER_ID,
					ApiConstants.USER_NOT_FOUND
				)));
			});
	}

	@Override
	public User patch(final UserPatchDto dto, final Long id) {
		try {
			final User user = findUserByIdOrThrow(id);
			final User updatedUser = objectMapper.updateValue(user, dto);
			return userRepository.save(updatedUser);
		} catch (JsonMappingException ex) {
			log.error("An error occurred while updating user with id [{}]", id);
			throw new RuntimeException("Failed to update user", ex);
		}
	}

	@Override
	public User update(final User sourceUser) {
		final User targetUser = findUserByIdOrThrow(sourceUser.getId());
		targetUser.setEmail(sourceUser.getEmail());
		targetUser.setAddress(sourceUser.getAddress());
		targetUser.setBirthday(sourceUser.getBirthday());
		targetUser.setFirstName(sourceUser.getFirstName());
		targetUser.setLastName(sourceUser.getLastName());
		targetUser.setPhoneNumber(sourceUser.getPhoneNumber());
		log.info("Updating user with email: [{}]", targetUser.getEmail());
		return this.save(targetUser);
	}

	@Override
	public Page<User> getByBirthdayRange(
		final LocalDate startDate,
		final LocalDate endDate,
		final Pageable pageable
	) {
		if (startDate.isAfter(endDate)) {
			throw new ResourceNotfoundException(List.of(new ApiError(
				ApiConstants.DATE,
				ApiConstants.WRONG_DATES_RANGE
			)));
		}
		Pageable result = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
		log.info("Getting page of users");
		return userRepository.getByBirthdayRange(startDate, endDate, result);
	}

	private void checkDuplicationEmail(final User sourceUser) {
		if (userRepository.findByEmail(sourceUser.getEmail()).isPresent()) {
			log.error("Attempted to save user with duplicate email: [{}]", sourceUser.getEmail());
			throw new DuplicateEmailException(List.of(new ApiError(ApiConstants.EMAIL, ApiConstants.DUPLICATE_EMAIL)));
		}
	}

	private Long calculateAge(final User user) {
		return ChronoUnit.YEARS.between(user.getBirthday(), LocalDate.now());
	}
}

