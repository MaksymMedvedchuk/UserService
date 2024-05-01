package com.clearsolutions.userservice.core.service.impl;

import com.clearsolutions.userservice.controller.UserController;
import com.clearsolutions.userservice.core.domain.dto.UserDto;
import com.clearsolutions.userservice.core.domain.dto.UserPatchDto;
import com.clearsolutions.userservice.core.service.LinksProvider;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class LinksProviderImpl implements LinksProvider {
	@Override
	public List<Link> addLinksToCreate(final UserDto userDto) {
		final List<Link> links = new ArrayList<>();
		links.add(WebMvcLinkBuilder.linkTo(methodOn(UserController.class).delete(userDto.getId())).withRel("delete"));
		links.add(linkTo(methodOn(UserController.class).patch(new UserPatchDto(), userDto.getId())).withRel("patch"));
		links.add(linkTo(methodOn(UserController.class).update(new UserDto(), userDto.getId())).withRel("update"));
		links.add(linkTo(methodOn(UserController.class).create(userDto)).withSelfRel());
		return links;
	}

	@Override
	public List<Link> addLinksToUpdate(final UserDto userDto) {
		final List<Link> links = new ArrayList<>();
		links.add(linkTo(methodOn(UserController.class).delete(userDto.getId())).withRel("delete"));
		links.add(linkTo(methodOn(UserController.class).patch(new UserPatchDto(), userDto.getId())).withRel("patch"));
		links.add(linkTo(methodOn(UserController.class).create(new UserDto())).withRel("create"));
		links.add(linkTo(methodOn(UserController.class).update(new UserDto(), userDto.getId())).withSelfRel());
		return links;
	}

	@Override
	public List<Link> addLinksToPatch(final UserDto userDto) {
		final List<Link> links = new ArrayList<>();
		links.add(linkTo(methodOn(UserController.class).delete(userDto.getId())).withRel("delete"));
		links.add(linkTo(methodOn(UserController.class).update(new UserDto(), userDto.getId())).withRel("update"));
		links.add(linkTo(methodOn(UserController.class).create(new UserDto())).withRel("create"));
		links.add(linkTo(methodOn(UserController.class).patch(new UserPatchDto(), userDto.getId())).withSelfRel());
		return links;
	}
}
