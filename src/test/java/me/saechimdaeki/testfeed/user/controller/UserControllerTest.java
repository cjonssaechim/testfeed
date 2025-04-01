package me.saechimdaeki.testfeed.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.saechimdaeki.testfeed.annotation.IntegrationTest;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.domain.UserType;
import me.saechimdaeki.testfeed.user.infrastructure.UserJpaRepository;
import me.saechimdaeki.testfeed.user.service.request.UserCreateRequest;

@IntegrationTest
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Test
	@DisplayName("사용자를 정상적으로 등록하면 사용자의 정보와 함께 정의한 응답을 반환한다.")
	void createUserTest() throws Exception {
		// given
		String mbrName = "testMbrName";
		String nickName = "testNickName";
		String type = "admin";

		UserCreateRequest userCreateRequest = new UserCreateRequest(mbrName, type, nickName);

		// when then
		mockMvc.perform(post("/users")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(userCreateRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("resultCode").value("002"))
			.andExpect(jsonPath("data.mbrName").value(mbrName))
			.andExpect(jsonPath("data.nickName").value(nickName))
			.andExpect(jsonPath("data.type").value(UserType.ADMIN.name()));
	}

	@Test
	@DisplayName("사용자 권한을 정상적으로 수정하면 성공 응답을 반환한다.")
	void updateUserTest() throws Exception {
		// given
		String mbrName = "testMbrName";
		String nickName = "testNickName";
		String type = "admin";

		userJpaRepository.save(new User(mbrName, UserType.fromString(type), nickName));

		String newType = "member";

		String requestBody = String.format("{\"mbrName\": \"%s\", \"type\": \"%s\"}", mbrName, newType);

		// when then
		mockMvc.perform(patch("/users")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("resultCode").value("001"));
	}


	@AfterEach
	void clearData(){
		userJpaRepository.deleteAll();
	}
}