package ru.open.telegram;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

import java.util.function.BiConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class FilmBot extends AbilityBot {

  private ResponseHandler responseHandler;

  public FilmBot(Environment environment) {
    super(environment.getProperty("BOT_TOKEN"), environment.getProperty("BOT_USERNAME"));
  }

  @Autowired
  public void setResponseHandler(ResponseHandler responseHandler) {
    this.responseHandler = responseHandler;
  }

  public Ability startBot() {
    return Ability
        .builder()
        .name("start")
        .info(Constants.START_DESCRIPTION)
        .locality(USER)
        .privacy(PUBLIC)
        .action(ctx -> responseHandler.replyToStart(ctx.chatId(), ctx.user()))
        .build();
  }

  public Reply replyToButtons() {
    BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) -> responseHandler.replyToButtons(
        getChatId(upd), upd.getMessage());
    return Reply.of(action, Flag.TEXT, upd -> responseHandler.userIsActive(getChatId(upd)));
  }

  @Override
  public long creatorId() {
    return 1L;
  }

}