package me.saechimdaeki.testfeed.user.service.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record UserChangeRoleRequest(
	@NotEmpty
	String mbrName,
	@NotEmpty
	@Schema(description = "유저 타입 (ADMIN, USER, BIZUSER 중 하나)", example = "USER", allowableValues = {"ADMIN", "USER", "BIZUSER"})
	String type
) {
}
