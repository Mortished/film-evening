package ru.open.telegram;

import java.util.HashMap;
import java.util.Map;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import ru.open.service.FilmService;
import ru.open.service.UserService;
import ru.open.utils.Constants;

public class ResponseHandler {

  private final SilentSender sender;
  private final Map<Long, UserState> chatStates;
  private final Map<String, String> selectedFilm;
  private final UserService userService;
  private final FilmService filmService;

  public ResponseHandler(FilmBot filmBot, UserService userService, FilmService filmService) {
    this.sender = filmBot.silent();
    chatStates = filmBot.db().getMap(Constants.CHAT_STATES);
    selectedFilm = new HashMap<>();
    this.userService = userService;
    this.filmService = filmService;
  }

  public void replyToStart(long chatId, User user) {
    userService.registerOrGetUser(user.getUserName());
    actionWithKeyboard(chatId, Constants.START_TEXT, KeyboardFactory.getMainChooseKeyboard(),
        UserState.AWAITING_MAIN_CHOOSE);
  }

  public void replyToButtons(long chatId, Message message) {
    if (message.getText().equalsIgnoreCase("/stop")) {
      stopChat(chatId);
    }

    switch (chatStates.get(chatId)) {
      case AWAITING_MAIN_CHOOSE -> replyToMainChoose(chatId, message);
      case AWAITING_FILM -> replyToAddFilm(chatId, message);
      case AWAITING_FILM_REMOVE -> replyToFilmRemove(chatId, message);
      default -> unexpectedMessage(chatId);
    }
  }

  private void unexpectedMessage(long chatId) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.setText(Constants.UNKNOWN_MESSAGE);
    sender.execute(sendMessage);
  }

  private void stopChat(long chatId) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.setText(Constants.EXIT_MESSAGE);
    chatStates.remove(chatId);
    sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
    sender.execute(sendMessage);
  }

  private void actionWithKeyboard(long chatId, String text, ReplyKeyboard YesOrNo,
      UserState state) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.setText(text);
    sendMessage.setReplyMarkup(YesOrNo);
    sender.execute(sendMessage);
    chatStates.put(chatId, state);
  }

  private void replyToMainChoose(long chatId, Message message) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);

    if (Constants.ADD_FILM_BUTTON.equalsIgnoreCase(message.getText())) {
      sendMessage.setText(Constants.ADD_FILM_DESCRIPTION);
      sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
      sender.execute(sendMessage);
      chatStates.put(chatId, UserState.AWAITING_FILM);
    } else if (Constants.CHOOSE_FILM_BUTTON.equalsIgnoreCase(message.getText())) {

      String login = message.getFrom().getUserName();
      String film = filmService.getRandomUserFilm(login);

      if (film.equals(Constants.NO_FILM_FOR_USER)) {
        sendMessage.setText(Constants.NO_FILM_FOR_USER);
        sendMessage.setReplyMarkup(KeyboardFactory.getMainChooseKeyboard());
        sender.execute(sendMessage);
        chatStates.put(chatId, UserState.AWAITING_MAIN_CHOOSE);
        return;
      }

      selectedFilm.put(login, film);

      sendMessage.setText("HERE WE GO:\n" + film);
      sendMessage.setReplyMarkup(KeyboardFactory.getFilmDeleteKeyboard());
      sender.execute(sendMessage);
      chatStates.put(chatId, UserState.AWAITING_FILM_REMOVE);
    } else {
      sendMessage.setText(Constants.CHOOSE_FROM_DIALOG_MSG);
      sendMessage.setReplyMarkup(KeyboardFactory.getMainChooseKeyboard());
      sender.execute(sendMessage);
      chatStates.put(chatId, UserState.AWAITING_MAIN_CHOOSE);
    }
  }

  private void replyToFilmRemove(long chatId, Message message) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    if (Constants.REMOVE_YES_BUTTON.equalsIgnoreCase(message.getText())) {
      sendMessage.setText(Constants.EXIT_MESSAGE);
      chatStates.remove(chatId);

      String login = message.getFrom().getUserName();
      filmService.deleteFilm(login, selectedFilm.get(login));
      selectedFilm.remove(login);

      sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
      sender.execute(sendMessage);
    } else if (Constants.REMOVE_NO_BUTTON.equalsIgnoreCase(message.getText())) {
      sendMessage.setText(Constants.REMOVE_NO_DESCRIPTION);
      sendMessage.setReplyMarkup(KeyboardFactory.getMainChooseKeyboard());
      sender.execute(sendMessage);
      chatStates.put(chatId, UserState.AWAITING_MAIN_CHOOSE);
    } else {
      sendMessage.setText(Constants.CHOOSE_FROM_DIALOG_MSG);
      sendMessage.setReplyMarkup(KeyboardFactory.getFilmDeleteKeyboard());
      sender.execute(sendMessage);
    }
  }

  private void replyToAddFilm(long chatId, Message message) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.setReplyMarkup(KeyboardFactory.getMainChooseKeyboard());
    sendMessage.setText(Constants.SAVE_FILM);
    filmService.saveFilm(message.getFrom().getUserName(), message.getText()); //save to database
    sender.execute(sendMessage);
    chatStates.put(chatId, UserState.AWAITING_MAIN_CHOOSE);
  }

  public boolean userIsActive(Long chatId) {
    return chatStates.containsKey(chatId);
  }

}