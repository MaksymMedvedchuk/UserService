package com.example.userservice.core.service;

import com.example.userservice.core.domain.dto.UserDto;
import org.springframework.hateoas.Link;

import java.util.List;

public interface LinksProvider {

	List<Link> addLinksToCreate(UserDto userDto);

	List<Link> addLinksToUpdate(UserDto userDto);

	List<Link> addLinksToPatch(UserDto userDto);
}
