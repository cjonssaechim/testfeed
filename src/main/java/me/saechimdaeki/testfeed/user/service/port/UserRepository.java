package me.saechimdaeki.testfeed.user.service.port;

import java.util.Optional;

import me.saechimdaeki.testfeed.user.domain.User;

public interface UserRepository {
	Optional<User> findByMbrName(String mbrName);

	User createUser(User user);

	Optional<User> findById(Long userId);
}
