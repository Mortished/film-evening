package ru.open.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.open.entity.User;
import ru.open.repository.UserRepository;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  public void registerOrGetUser(String login) {
    Optional<User> user = userRepository.findByLogin(login);

    if (user.isEmpty()) {
      userRepository.save(User.builder()
          .login(login)
          .build());
    }

  }

}
