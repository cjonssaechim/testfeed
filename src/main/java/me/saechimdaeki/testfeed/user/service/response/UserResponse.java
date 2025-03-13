package me.saechimdaeki.testfeed.user.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saechimdaeki.testfeed.user.domain.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	private Long mbrNo;
	private String mbrName;
	private String type;
	private String profile;

	public static UserResponse from(User user) {
		return new UserResponse(user.getId(), user.getUsername(), user.getUserType().name(), user.getProfileUrl());
	}
}
