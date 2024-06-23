package ru.open.telegram;

import java.util.List;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class KeyboardFactory {

  public static ReplyKeyboard getMainChooseKeyboard() {
    KeyboardRow row = new KeyboardRow();
    row.add(Constants.ADD_FILM_BUTTON);
    row.add(Constants.CHOOSE_FILM_BUTTON);
    return new ReplyKeyboardMarkup(List.of(row));
  }

  public static ReplyKeyboard getFilmDeleteKeyboard() {
    KeyboardRow row = new KeyboardRow();
    row.add(Constants.REMOVE_YES_BUTTON);
    row.add(Constants.REMOVE_NO_BUTTON);
    return new ReplyKeyboardMarkup(List.of(row));
  }

}
