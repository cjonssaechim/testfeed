package me.saechimdaeki.testfeed.common.exception;

import java.io.Serial;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 340254415700516296L;
	ErrorCode errorCode;

	public BaseException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}