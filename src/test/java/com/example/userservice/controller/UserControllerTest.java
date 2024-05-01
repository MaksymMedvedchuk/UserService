package com.example.userservice.controller;

import com.example.userservice.core.converter.UserDataConverter;
import com.example.userservice.core.domain.dto.ResponseDto;
import com.example.userservice.core.domain.dto.UserDto;
import com.example.userservice.core.domain.dto.UserPatchDto;
import com.example.userservice.core.domain.entity.User;
import com.example.userservice.core.service.LinksProvider;
import com.example.userservice.core.service.UserService;
import com.example.userservice.core.service.exception.ResourceNotfoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private UserDataConverter converter;

	@MockBean
	private LinksProvider links;

	@Autowired
	private ObjectMapper objectMapper;

	private UserDto userDtoWithId;

	private UserDto userDtoNoId;

	private User userWithId;

	private User userNoId;

	private Long id;

	private UserDto invalidData;

	private UserPatchDto withUpdateField;

	private UserDto withUpdatedField;

	private User updatedUser;

	private UserDto updatedUserDto;

	@BeforeEach
	void setUp() {
		id = 1L;
		userDtoWithId = createUserDtoWithId(id);
		userDtoNoId = createUserDtoNoId();
		userWithId = createUserWithId(id);
		userNoId = createUserNoId();
		invalidData = createUserNoIdWithInvalidData();
		withUpdateField = createUserWithUpdateField();
		withUpdatedField = createUserWithUpdatedField();
		updatedUser = createUpdateUser();
		updatedUserDto = createUpdatedUserDto();
	}

	@Test
	public void testCreate_ifValidData() throws Exception {
		when(converter.convertToEntityAttribute(userDtoNoId)).thenReturn(userNoId);
		when(userService.save(userNoId)).thenReturn(userWithId);
		when(converter.convertToDatabaseColumn(userWithId)).thenReturn(userDtoWithId);

		MvcResult result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDtoWithId)))
			.andExpect(status().isCreated())
			.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		ResponseDto response = objectMapper.readValue(jsonResponse, new TypeReference<ResponseDto>() {
		});
		response.getData().setId(id);
		assertEquals(response.getData(), userDtoWithId);
		verify(userService, times(1)).save(userNoId);
		verify(converter, times(1)).convertToEntityAttribute(userDtoNoId);
		verify(converter, times(1)).convertToDatabaseColumn(userWithId);
	}

	@Test
	public void testCreate_ifInvalidData() throws Exception {
		mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidData)))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testDelete_ifExist() throws Exception {
		doNothing().when(userService).delete(id);
		mockMvc.perform(delete("/users/{id}", id))
			.andExpect(status().isOk());
		verify(userService, times(1)).delete(id);
	}

	@Test
	public void testDelete_ifDoesNotExist() throws Exception {
		doThrow(ResourceNotfoundException.class).when(userService).delete(id);
		when(userService.findUserByIdOrThrow(id)).thenReturn(null);

		mockMvc.perform(delete("/users/{id}", id))
			.andExpect(status().isUnprocessableEntity());

		verify(userService, times(1)).delete(id);
	}

	@Test
	public void testPatch_ifUpdateOneField() throws Exception {
		when(userService.patch(withUpdateField, id)).thenReturn(userWithId);
		when(converter.convertToDatabaseColumn(userWithId)).thenReturn(withUpdatedField);

		mockMvc.perform(patch("/users/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(withUpdateField)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data.email", is(withUpdatedField.getEmail())))
			.andExpect(jsonPath("data.firstName", is(withUpdatedField.getFirstName())))
			.andExpect(jsonPath("data.lastName", is(withUpdatedField.getLastName())))
			.andExpect(jsonPath("data.birthday", is(withUpdatedField.getBirthday().toString())))
			.andExpect(jsonPath("data.address", is(withUpdatedField.getAddress())))
			.andExpect(jsonPath("data.phoneNumber", is(withUpdatedField.getPhoneNumber())));

		verify(userService, times(1)).patch(withUpdateField, id);
		verify(converter, times(1)).convertToDatabaseColumn(userWithId);
	}

	@Test
	public void testPatch_ifUserDoesNotNotExist() throws Exception {
		doThrow(ResourceNotfoundException.class).when(userService).patch(withUpdateField, id);
		when(userService.findUserByIdOrThrow(id)).thenReturn(null);

		mockMvc.perform(patch("/users/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(withUpdateField)))
			.andExpect(status().isUnprocessableEntity());

		verify(userService, times(1)).patch(withUpdateField, id);
	}

	@Test
	public void testUpdate_ifValidData() throws Exception {
		when(converter.convertToEntityAttribute(updatedUserDto)).thenReturn(updatedUser);
		when(userService.update(updatedUser)).thenReturn(updatedUser);
		when(converter.convertToDatabaseColumn(updatedUser)).thenReturn(updatedUserDto);

		MvcResult result = mockMvc.perform(put("/users/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedUserDto)))
			.andExpect(status().isOk())
			.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		ResponseDto response = objectMapper.readValue(jsonResponse, new TypeReference<ResponseDto>() {
		});
		assertEquals(response.getData(), updatedUserDto);
		verify(userService, times(1)).update(updatedUser);
		verify(converter, times(1)).convertToEntityAttribute(updatedUserDto);
		verify(converter, times(1)).convertToDatabaseColumn(updatedUser);
	}

	@Test
	public void testUpdate_ifInvalidData() throws Exception {
		mockMvc.perform(put("/users/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidData)))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testGetByBirthdayRange_ifValidData() throws Exception {
		mockMvc.perform(get("/users")
				.param("startDate", "2023-10-10")
				.param("endDate", "2023-11-11")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	public void testGetByBirthdayRange_ifInvalidData() throws Exception {
		mockMvc.perform(get("/users")
				.param("startDate", "2023-10-10")
				.param("endDate", "2023-9-9")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	private User createUserNoId() {
		return createUserWithId(null);
	}

	private User createUserWithId(Long id) {
		return new User(id, "test@gmail.com", "Java", "Test",
			LocalDate.of(2000, 1, 1), "Kyiv", "380991234568"
		);
	}

	private UserDto createUserDtoNoId() {
		return createUserDtoWithId(null);
	}

	private UserDto createUserDtoWithId(Long id) {
		return new UserDto(id, "test@gmail.com", "Java", "Test",
			LocalDate.of(2000, 1, 1), "Kyiv", "380991234568"
		);
	}

	private UserDto createUserNoIdWithInvalidData() {
		return new UserDto("test", "Java", "Test",
			LocalDate.of(2024, 1, 1), "Kyiv", "380991234568"
		);
	}

	private UserPatchDto createUserWithUpdateField() {
		return new UserPatchDto(null, null,
			null, null, "Lviv", null
		);
	}

	private UserDto createUserWithUpdatedField() {
		return new UserDto(id, "test@gmail.com", "Java", "Test",
			LocalDate.of(2000, 1, 1), "Lviv", "380991234568"
		);
	}

	private UserDto createUpdatedUserDto() {
		return new UserDto("test@gmail.com", "Java", "Test",
			LocalDate.of(2000, 1, 1), "Kyiv", "380991234568"
		);
	}

	private User createUpdateUser() {
		return new User("test@gmail.com", "Java", "Test",
			LocalDate.of(2000, 1, 1), "Kyiv", "380991234568"
		);
	}
}
