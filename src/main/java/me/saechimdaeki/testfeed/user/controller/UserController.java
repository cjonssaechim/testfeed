package me.saechimdaeki.testfeed.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.common.domain.response.CommonResponse;
import me.saechimdaeki.testfeed.common.exception.ErrorResponseEntity;
import me.saechimdaeki.testfeed.user.service.UserService;
import me.saechimdaeki.testfeed.user.service.request.UserChangeRoleRequest;
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
	@ApiResponse(responseCode = "201", description = "유저 생성 성공",
		content = @Content(schema = @Schema(implementation = UserResponse.class)))
	public CommonResponse<UserResponse> createUser(
		@Parameter(description = "사용자 생성 요청 데이터") @RequestBody UserCreateRequest userCreateRequest) {
		return CommonResponse.of(HttpStatus.CREATED.value(), userService.createUser(userCreateRequest));
	}

	@Operation(summary = "사용자 권한 수정", description = "이미 만들어진 사용자에 대한 유저 권한 수정")
	@PatchMapping
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "유저 권한 업데이트 성공",
			content = @Content(schema = @Schema(implementation = Void.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(schema = @Schema(implementation = ErrorResponseEntity.class))),
		@ApiResponse(responseCode = "500", description = "서버 오류",
			content = @Content(schema = @Schema(implementation = Void.class))),
	})
	public CommonResponse<Void> updateUser(@RequestBody @Valid UserChangeRoleRequest userChangeRoleRequest) {
		userService.changeUserRole(userChangeRoleRequest);
		return CommonResponse.of(HttpStatus.OK.value(), null);
	}

	@PatchMapping("/{userId}")
	@Operation(summary = "유저 프로필 이미지 수정", description = "이미 만들어진 사용자에 대한 프로필 이미지 수정")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "유저 권한 업데이트 성공",
			content = @Content(schema = @Schema(implementation = Void.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(schema = @Schema(implementation = ErrorResponseEntity.class)))
	})
	public CommonResponse<UserResponse> changeUserRole(@PathVariable Long userId,
		@RequestPart(value = "image", required = false) MultipartFile image) {
		userService.changeUserProfile(userId, image);
		return CommonResponse.of(HttpStatus.OK.value(), null);
	}

}
