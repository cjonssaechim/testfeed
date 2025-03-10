package me.saechimdaeki.testfeed.user.service.request;

public record UserChangeRoleRequest(
	String username,
	String userType
) {
}
