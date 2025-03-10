package me.saechimdaeki.testfeed.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.service.port.UserRepository;
import me.saechimdaeki.testfeed.user.service.request.UserChangeRoleRequest;
import me.saechimdaeki.testfeed.user.service.request.UserCreateRequest;
import me.saechimdaeki.testfeed.user.service.response.UserResponse;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public UserResponse createUser(UserCreateRequest userCreateRequest) {
		final User user = userRepository.createUser(UserCreateRequest.createUser(userCreateRequest));
		return UserResponse.from(user);
	}

	@Transactional
	public void changeUserRole(final UserChangeRoleRequest userChangeRoleRequest) {
		final User user = userRepository.findByUserName(userChangeRoleRequest.username())
			.orElseThrow(() -> new RuntimeException("User not found"));
		user.changeUserType(userChangeRoleRequest.userType());
	}

}
