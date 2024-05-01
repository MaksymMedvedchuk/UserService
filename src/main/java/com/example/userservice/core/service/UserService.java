package com.example.userservice.core.service;

import com.example.userservice.core.domain.dto.UserPatchDto;
import com.example.userservice.core.domain.entity.User;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface UserService {

	User save(User user);

	void delete(Long id);

	User patch(UserPatchDto dto, Long id);

	User update(User user);

	Page<User> getByBirthdayRange(LocalDate startDate, LocalDate endDate, Integer page, Integer limit);

	User findUserByIdOrThrow(final Long id);
}
