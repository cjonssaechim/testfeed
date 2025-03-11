package me.saechimdaeki.testfeed.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.common.domain.response.CommonResponse;
import me.saechimdaeki.testfeed.user.service.UserService;
import me.saechimdaeki.testfeed.user.service.request.UserCreateRequest;
import me.saechimdaeki.testfeed.user.service.response.UserResponse;

@Tag(name = "사용자 관리", description = "사용자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
	@PostMapping
	public CommonResponse<UserResponse> createUser(
		@Parameter(description = "사용자 생성 요청 데이터") @RequestBody UserCreateRequest userCreateRequest) {
		return CommonResponse.of(HttpStatus.CREATED.value(), userService.createUser(userCreateRequest));
	}

}
