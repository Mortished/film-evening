package ru.open;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.open.telegram.FilmBot;

@SpringBootApplication
public class FilmEveningApp {

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx = SpringApplication.run(FilmEveningApp.class, args);
    try {
      TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
      botsApi.registerBot(ctx.getBean("filmBot", FilmBot.class));
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  @Bean
  public ModelMapper modelMapperBean() {
    return new ModelMapper();
  }

}
