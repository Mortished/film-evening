package ru.open.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.open.entity.User;
import ru.open.model.dto.NewUserDto;
import ru.open.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  public User createUser(@Validated @RequestBody NewUserDto user) {
    return userRepository.save(modelMapper.map(user, User.class));
  }

}
