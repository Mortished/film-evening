package ru.open.telegram;

import java.util.Map;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;


public class ResponseHandler {

  private final SilentSender sender;
  private final Map<Long, UserState> chatStates;

  public ResponseHandler(SilentSender sender, DBContext db) {
    this.sender = sender;
    chatStates = db.getMap(Constants.CHAT_STATES);
  }

  public void replyToStart(long chatId) {
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
    sendMessage.setText("I did not expect that.");
    sender.execute(sendMessage);
  }

  private void stopChat(long chatId) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.setText("Thank you for your order. See you soon!\nPress /start to order again");
    chatStates.remove(chatId);
    sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
    sender.execute(sendMessage);
  }

  private void actionWithKeyboard(long chatId, String text, ReplyKeyboard YesOrNo,
      UserState awaitingReorder) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.setText(text);
    sendMessage.setReplyMarkup(YesOrNo);
    sender.execute(sendMessage);
    chatStates.put(chatId, awaitingReorder);
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
      sendMessage.setText("You get John Wik: Chapter 4!");
      sendMessage.setReplyMarkup(KeyboardFactory.getFilmDeleteKeyboard());
      sender.execute(sendMessage);
      chatStates.put(chatId, UserState.AWAITING_FILM_REMOVE);
    } else {
      sendMessage.setText("Please select from the options below.");
      sendMessage.setReplyMarkup(KeyboardFactory.getMainChooseKeyboard());
      sender.execute(sendMessage);
    }
  }

  private void replyToFilmRemove(long chatId, Message message) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    if (Constants.REMOVE_YES_BUTTON.equalsIgnoreCase(message.getText())) {
      sendMessage.setText(Constants.EXIT_MESSAGE);
      chatStates.remove(chatId);
      //TODO add remove from database
      sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
      sender.execute(sendMessage);
    } else if (Constants.REMOVE_NO_BUTTON.equalsIgnoreCase(message.getText())) {
      sendMessage.setText(Constants.REMOVE_NO_DESCRIPTION);
      sendMessage.setReplyMarkup(KeyboardFactory.getMainChooseKeyboard());
      sender.execute(sendMessage);
      chatStates.put(chatId, UserState.AWAITING_FILM_REMOVE);
    } else {
      sendMessage.setText("Please select from the options below.");
      sendMessage.setReplyMarkup(KeyboardFactory.getFilmDeleteKeyboard());
      sender.execute(sendMessage);
    }
  }

  private void replyToAddFilm(long chatId, Message message) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.setReplyMarkup(KeyboardFactory.getMainChooseKeyboard());
    sendMessage.setText(Constants.SAVE_FILM);
    //TODO add save to database
    sender.execute(sendMessage);
    chatStates.put(chatId, UserState.AWAITING_MAIN_CHOOSE);
  }

  public boolean userIsActive(Long chatId) {
    return chatStates.containsKey(chatId);
  }

}