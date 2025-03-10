package me.saechimdaeki.testfeed.user.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.domain.UserType;

@Getter
@AllArgsConstructor
public class UserCreateRequest {
	private String username;
	private String userType;

	public static User createUser(UserCreateRequest userCreateRequest) {
		return new User(userCreateRequest.username,
			UserType.fromString(userCreateRequest.userType));
	}
}
