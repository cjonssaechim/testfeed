package me.saechimdaeki.testfeed.common.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommonResponse<T> {
	int statusCode;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	String message;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	T data;

	public static <T> CommonResponse<T> of(int statusCode, T data) {
		return new CommonResponse<>(statusCode, "success", data);
	}
}
