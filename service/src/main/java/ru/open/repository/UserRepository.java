package ru.open.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.open.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
