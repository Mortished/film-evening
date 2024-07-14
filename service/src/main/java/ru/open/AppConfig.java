package ru.open;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ru.open.service.FilmService;
import ru.open.service.UserService;
import ru.open.telegram.FilmBot;
import ru.open.telegram.ResponseHandler;

@Configuration
public class AppConfig {

  @Bean
  public ResponseHandler responseHandler(FilmBot filmBot, UserService userService, FilmService filmService) {
        return new ResponseHandler(filmBot, userService, filmService);
  }

  @Bean
  public FilmBot filmBot(Environment environment, UserService userService, FilmService filmService) {
    FilmBot filmBot = new FilmBot(environment);
    // вызов метода для внедрения ResponseHandler
    filmBot.setResponseHandler(responseHandler(filmBot, userService, filmService));
    return filmBot;
  }

}
