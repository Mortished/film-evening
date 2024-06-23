package ru.open.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

  //private final UserRepository userRepository;
  private final ModelMapper modelMapper;

//  public User createUser(@Validated @RequestBody NewUserDto user) {
//    return userRepository.save(modelMapper.map(user, User.class));
//  }

}
