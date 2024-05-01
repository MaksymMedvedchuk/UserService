package com.example.userservice.core.converter;

import com.example.userservice.core.domain.dto.UserDto;
import com.example.userservice.core.domain.entity.User;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserDataCreateConverter implements AttributeConverter<User, UserDto> {

	private final ModelMapper modelMapper;

	public UserDataCreateConverter(final ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@Override
	public UserDto convertToDatabaseColumn(final User entity) {
		log.info("Converting User entity to UserDto: [{}]", entity);
		return modelMapper.map(entity, UserDto.class);
	}

	@Override
	public User convertToEntityAttribute(final UserDto userDto) {
		log.info("Converted UserDto to User entity: [{}]", userDto);
		return modelMapper.map(userDto, User.class);
	}
}

