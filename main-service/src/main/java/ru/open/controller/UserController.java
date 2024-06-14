package ru.open.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.open.entity.User;
import ru.open.model.dto.NewUserDto;
import ru.open.model.dto.UserDto;
import ru.open.repository.UserRepository;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @PostMapping
  public UserDto createUser(@Validated @RequestBody NewUserDto user) {
    return modelMapper.map(userRepository.save(modelMapper.map(user, User.class)), UserDto.class);
  }


}
