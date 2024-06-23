package ru.open.service;

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

  public User createUser(String login) {
    return new User();
  }

}
