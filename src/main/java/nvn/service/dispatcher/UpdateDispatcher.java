package nvn.service.dispatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nvn.service.handler.CallbackHandler;
import nvn.service.handler.CommandHandler;
import nvn.service.handler.MessageHandler;
import nvn.bot.DietTelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateDispatcher {

  private final CommandHandler commandHandler;
  private final MessageHandler messageHandler;
  private final CallbackHandler callbackHandler;

  public void dispatch(Update update, DietTelegramBot bot) {
    try {
      if (update.hasCallbackQuery()) {
        callbackHandler.handle(update, bot);
      } else if (update.hasMessage()
          && update.getMessage().hasText()
          && update.getMessage().getText().startsWith("/")) {
        commandHandler.handle(update, bot);
      } else {
        messageHandler.handle(update, bot);
      }
    } catch (Exception e) {
      log.error("Error dispatching update", e);
    }
  }
}
