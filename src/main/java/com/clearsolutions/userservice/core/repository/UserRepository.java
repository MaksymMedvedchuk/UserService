package com.clearsolutions.userservice.core.repository;

import com.clearsolutions.userservice.core.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.birthday BETWEEN :startRange AND :endRange")
	Page<User> getByBirthdayRange(
		@Param("startRange") LocalDate startRange,
		@Param("endRange") LocalDate endRange,
		Pageable pageable
	);
}
