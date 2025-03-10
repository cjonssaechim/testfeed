package me.saechimdaeki.testfeed.user.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.saechimdaeki.testfeed.user.domain.User;

@Getter
@AllArgsConstructor
public class UserResponse {
	private String username;
	private String userType;

	public static UserResponse from(User user) {
		return new UserResponse(user.getUsername(), user.getUserType().name());
	}
}
