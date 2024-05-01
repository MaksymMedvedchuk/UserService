package com.clearsolutions.userservice.core.service;

import com.clearsolutions.userservice.core.domain.dto.UserPatchDto;
import com.clearsolutions.userservice.core.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface UserService {

	User save(User user);

	void delete(Long id);

	User patch(UserPatchDto dto, Long id);

	User update(User user);

	Page<User> getByBirthdayRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

	User findUserByIdOrThrow(final Long id);
}
