package ru.open.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewUserDto {

  @NotBlank
  private String login;

}
