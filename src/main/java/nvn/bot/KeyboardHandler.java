package nvn.bot;

import java.util.ArrayList;
import java.util.List;
import nvn.models.enums.Command;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class KeyboardHandler {
  private final List<KeyboardRow> keyboardRows = new ArrayList<>();
  private KeyboardRow currentRow = new KeyboardRow();

  private KeyboardHandler() {
  }

  public static KeyboardHandler builder() {
    return new KeyboardHandler();
  }

  public KeyboardHandler addButton(Command button) {
    currentRow.add(new KeyboardButton(button.getCommand()));
    return this;
  }

  public KeyboardHandler newRow() {
    keyboardRows.add(currentRow);
    currentRow = new KeyboardRow();
    return this;
  }

  public ReplyKeyboardMarkup build() {
    if (!currentRow.isEmpty()) {
      keyboardRows.add(currentRow);
    }
    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    keyboardMarkup.setKeyboard(keyboardRows);
    keyboardMarkup.setResizeKeyboard(true);
    return keyboardMarkup;
  }
}

