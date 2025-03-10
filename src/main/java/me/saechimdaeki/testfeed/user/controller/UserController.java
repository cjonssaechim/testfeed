package me.saechimdaeki.testfeed.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.user.service.UserService;
import me.saechimdaeki.testfeed.user.service.request.UserCreateRequest;
import me.saechimdaeki.testfeed.user.service.response.UserResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	@PostMapping
	public UserResponse createUser(@RequestBody UserCreateRequest userCreateRequest) {
		return userService.createUser(userCreateRequest);
	}
}
