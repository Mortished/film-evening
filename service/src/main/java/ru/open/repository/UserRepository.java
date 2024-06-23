package ru.open.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.open.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByLogin(String login);

}
