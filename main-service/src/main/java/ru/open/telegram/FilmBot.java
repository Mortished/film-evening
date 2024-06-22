package ru.open.telegram;

import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;

@Component
public class FilmBot extends AbilityBot {

  public FilmBot() {
    super("6786996916:AAHuivN8bKWG3Uo7jvfD5pg4HgRjG1tmf6o", "filmEveningBot");
  }

  @Override
  public long creatorId() {
    return 1L;
  }

  public Ability startBot() {
    return Ability
        .builder()
        .name("start")
        .info("Constants.START_DESCRIPTION")
        .build();
  }


}