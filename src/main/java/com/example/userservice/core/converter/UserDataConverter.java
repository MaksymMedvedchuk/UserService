package com.example.userservice.core.converter;

import com.example.userservice.core.domain.dto.CustomPage;
import com.example.userservice.core.domain.dto.ResponseDto;
import com.example.userservice.core.domain.dto.UserDto;
import com.example.userservice.core.domain.entity.User;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserDataConverter implements AttributeConverter<User, UserDto> {

	private final ModelMapper modelMapper;

	public UserDataConverter(final ModelMapper modelMapper) {
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

	public CustomPage<ResponseDto> convertToPage(Page<User> users){
		CustomPage<ResponseDto> page = new CustomPage<>();
		List<ResponseDto> dtoList = users.stream()
				.map(user -> new ResponseDto(this.convertToDatabaseColumn(user)))
					.toList();

		page.setDto(dtoList);
		page.setCurrentPage(users.getNumber() + 1);
		page.setTotalItems(users.getTotalElements());
		page.setTotalPages(users.getTotalPages());
		page.setItemsPerPage(users.getNumberOfElements());
		log.info("Converted page of users to ResponseDto list");
		return page;
	}
}

