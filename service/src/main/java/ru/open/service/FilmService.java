package ru.open.service;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.open.entity.Film;
import ru.open.entity.User;
import ru.open.repository.FilmRepository;
import ru.open.repository.UserRepository;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {

  private final FilmRepository filmRepository;
  private final UserRepository userRepository;

  public List<String> getAllUserFilms(String login) {
    return filmRepository.findFilmsByUserLogin(login).stream()
        .map(Film::getName)
        .toList();
  }

  public void saveFilm(String login, String film) {
    User userEntity = userRepository.findByLogin(login)
        .orElseThrow(RuntimeException::new);

    Film filmEntity = Film.builder()
        .name(film)
        .user(userEntity)
        .build();

    filmRepository.save(filmEntity);
  }

  public void deleteFilm(String login, String film) {
    User userEntity = userRepository.findByLogin(login)
        .orElseThrow(RuntimeException::new);

    Film filmEntity = filmRepository.findFilmByUserLoginAndName(login, film);
    filmRepository.delete(filmEntity);
  }

  public String getRandomUserFilm(String login) {
    User userEntity = userRepository.findByLogin(login)
        .orElseThrow(RuntimeException::new);

    return filmRepository.getRandomFilmByUserLogin(login).getName();
  }

}
