package me.saechimdaeki.testfeed.post.exception;

import me.saechimdaeki.testfeed.common.exception.BaseException;
import me.saechimdaeki.testfeed.common.exception.ErrorCode;

public class PostException extends BaseException {
	public PostException(ErrorCode errorCode) {
		super(errorCode);
	}
}
