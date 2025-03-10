package me.saechimdaeki.testfeed.user.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.saechimdaeki.testfeed.user.domain.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);
}
