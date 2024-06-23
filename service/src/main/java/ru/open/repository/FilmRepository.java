package ru.open.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.open.entity.Film;

public interface FilmRepository extends JpaRepository<Film, Long> {

}
