package me.saechimdaeki.testfeed.user.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.saechimdaeki.testfeed.common.exception.ErrorCode;
import me.saechimdaeki.testfeed.common.file.FileStorageService;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.exception.UserException;
import me.saechimdaeki.testfeed.user.service.port.UserRepository;
import me.saechimdaeki.testfeed.user.service.request.UserChangeRoleRequest;
import me.saechimdaeki.testfeed.user.service.request.UserCreateRequest;
import me.saechimdaeki.testfeed.user.service.response.UserResponse;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final FileStorageService fileStorageService;

	@Transactional
	public UserResponse createUser(UserCreateRequest userCreateRequest) {
		final User user = userRepository.createUser(UserCreateRequest.createUser(userCreateRequest));
		return UserResponse.from(user);
	}

	@Transactional
	public void changeUserRole(final UserChangeRoleRequest userChangeRoleRequest) {
		final User user = userRepository.findByMbrName(userChangeRoleRequest.username())
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
		user.changeUserType(userChangeRoleRequest.userType());
	}

	@Transactional
	public void changeUserProfile(Long userId, MultipartFile image) {
		final User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

		if (image == null || image.isEmpty()) {
			return;
		}

		try {
			String profileUrl = fileStorageService.saveFile(image);
			user.changeProfileUrl(profileUrl);
		} catch (IOException e) {
			log.error("프로필 파일 저장 실패 ", e.getMessage());
		}
	}
}
