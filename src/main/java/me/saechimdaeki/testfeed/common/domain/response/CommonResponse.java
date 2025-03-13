package me.saechimdaeki.testfeed.common.domain.response;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public class CommonResponse<T> {
	String resultCode;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	String resultMessage;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	T data;

	public static <T> CommonResponse<T> of(int statusCode, T data) {
		return new CommonResponse<>(Status.getSpecificMessage(statusCode), "정상", data);
	}

	@RequiredArgsConstructor
	@Getter
	enum Status {
		SUCCESS(200, "001", "요청이 정상적으로 응답"),
		CREATED(201, "002", "요청한 자원이 정상적으로 생성"),
		NO_CONTENT(204, "003", "삭제가 완료되었을 시");

		private final int code;
		private final String message;
		private final String detail;

		private static final Map<Integer, String> MESSAGE_CACHE = Arrays.stream(values())
			.collect(Collectors.toMap(Status::getCode, Status::getMessage));

		public static String getSpecificMessage(int code) {
			return MESSAGE_CACHE.getOrDefault(code, SUCCESS.message);
		}
	}
}
