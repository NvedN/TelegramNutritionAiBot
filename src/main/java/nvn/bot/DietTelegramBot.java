package nvn.bot;

import lombok.RequiredArgsConstructor;
import nvn.service.dispatcher.UpdateDispatcher;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class DietTelegramBot extends TelegramLongPollingBot {

  private final String botUsername;
  private final UpdateDispatcher updateDispatcher;

  public DietTelegramBot(String botUsername, String botToken, UpdateDispatcher updateDispatcher) {
    super(botToken);
    this.botUsername = botUsername;
    this.updateDispatcher = updateDispatcher;
  }

  @Override
  public String getBotUsername() {
    return botUsername;
  }

  @Override
  public void onUpdateReceived(Update update) {
    updateDispatcher.dispatch(update, this);
  }
}
