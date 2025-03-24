package nvn.service.handler;

import lombok.RequiredArgsConstructor;
import nvn.service.TelegramBotService;
import nvn.bot.DietTelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class CallbackHandler {

  private final TelegramBotService botService;

  public void handle(Update update, DietTelegramBot bot) {
    CallbackQuery query = update.getCallbackQuery();
    String data = query.getData();
    Long chatId = query.getMessage().getChatId();

    botService.sendMessage(chatId, "You selected: " + data, bot);
    answerCallbackQuery(query, bot);
  }

  private void answerCallbackQuery(CallbackQuery callbackQuery, DietTelegramBot bot) {
    AnswerCallbackQuery response =
        AnswerCallbackQuery.builder()
            .callbackQueryId(callbackQuery.getId())
            .text("Your choice was received!")
            .showAlert(false)
            .build();

    try {
      bot.execute(response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
