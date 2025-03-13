package me.saechimdaeki.testfeed.user.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.saechimdaeki.testfeed.user.domain.User;
import me.saechimdaeki.testfeed.user.service.port.UserRepository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	@Override
	public Optional<User> findByUserName(String username) {
		return userJpaRepository.findByUsername(username);
	}

	@Override
	public User createUser(User user) {
		return userJpaRepository.save(user);
	}

	@Override
	public Optional<User> findById(Long userId) {
		return userJpaRepository.findById(userId);
	}
}
