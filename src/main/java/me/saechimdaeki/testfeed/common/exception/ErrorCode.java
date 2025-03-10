
package me.saechimdaeki.testfeed.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user-E001", "user not found check your username"),



    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "post-E001", "post not found check your post id"),


    POST_LIKE_DUPLICATED(HttpStatus.BAD_REQUEST, "post-E001", "post is already liked"),
    POST_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "post-E002", "post-like request not found check your postId and userId "),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}