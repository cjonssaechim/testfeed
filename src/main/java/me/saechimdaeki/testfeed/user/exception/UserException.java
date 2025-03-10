package me.saechimdaeki.testfeed.user.exception;

import me.saechimdaeki.testfeed.common.exception.BaseException;
import me.saechimdaeki.testfeed.common.exception.ErrorCode;

public class UserException extends BaseException {
	public UserException(ErrorCode errorCode) {
		super(errorCode);
	}
}
