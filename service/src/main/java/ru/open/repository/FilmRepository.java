package ru.open.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.open.entity.Film;

public interface FilmRepository extends JpaRepository<Film, Long> {

  List<Film> findFilmsByUserLogin(String userLogin);

  Film findFilmsByUserLoginAndName(String userLogin, String name);

}
