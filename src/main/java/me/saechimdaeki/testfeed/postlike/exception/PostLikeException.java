package me.saechimdaeki.testfeed.postlike.exception;

import me.saechimdaeki.testfeed.common.exception.BaseException;
import me.saechimdaeki.testfeed.common.exception.ErrorCode;

public class PostLikeException extends BaseException {
	public PostLikeException(ErrorCode errorCode) {
		super(errorCode);
	}
}
