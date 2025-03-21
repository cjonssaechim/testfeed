package me.saechimdaeki.testfeed.user.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.domain.UserType;

@Getter
@AllArgsConstructor
public class UserCreateRequest {
	private String mbrName;
	private String type;
	private String nickName;

	public static User createUser(UserCreateRequest userCreateRequest) {
		return new User(userCreateRequest.mbrName,
			UserType.fromString(userCreateRequest.type),
			userCreateRequest.nickName);
	}
}
