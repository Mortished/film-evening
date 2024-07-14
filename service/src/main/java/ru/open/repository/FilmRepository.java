package ru.open.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.open.entity.Film;

public interface FilmRepository extends JpaRepository<Film, Long> {

  List<Film> findFilmsByUserLogin(String userLogin);

  Film findFilmByUserLoginAndName(String userLogin, String name);

  @Query("select f from Film f where f.user.login = ?1 order by RAND() LIMIT 1")
  Film getRandomFilmByUserLogin(String userLogin);

}
