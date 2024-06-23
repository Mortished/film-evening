package ru.open.service;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.open.entity.Film;
import ru.open.repository.FilmRepository;
import ru.open.repository.UserRepository;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {

  private final FilmRepository filmRepository;
  private final UserRepository userRepository;

  public List<String> getAllUserFilms(User user) {
    return filmRepository.findFilmsByUserLogin(user.getUserName()).stream()
        .map(Film::getName)
        .toList();
  }

  public Film saveFilm(User user, String film) {
    ru.open.entity.User userEntity = userRepository.findById(user.getId())
        .orElseThrow(RuntimeException::new);

    Film filmEntity = Film.builder()
        .name(film)
        .user(userEntity)
        .build();

    return filmRepository.save(filmEntity);
  }

  public void deleteFilm(User user, String film) {
    ru.open.entity.User userEntity = userRepository.findById(user.getId())
        .orElseThrow(RuntimeException::new);

    Film filmEntity = filmRepository.findFilmsByUserLoginAndName(user.getUserName(), film);
    filmRepository.delete(filmEntity);
  }

}
