package ru.open.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.open.repository.FilmRepository;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {

  private final FilmRepository filmRepository;

}
