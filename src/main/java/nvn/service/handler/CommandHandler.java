package nvn.service.handler;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import nvn.bot.KeyboardHandler;
import nvn.service.TelegramBotService;
import nvn.bot.DietTelegramBot;
import nvn.data.repository.UserParametersRepository;
import nvn.models.enums.Command;
import nvn.service.PromptsService;
import nvn.service.ReportService;
import nvn.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class CommandHandler {

  private final UserParametersRepository userRepo;
  private final TelegramBotService botService;
  private final PromptsService promptsService;
  private final ReportService reportService;
  private final UserService userService;

  public void handle(Update update, DietTelegramBot bot) {
    String commandText = update.getMessage().getText();
    Long chatId = update.getMessage().getChatId();
    Locale locale = getUserLocale(update);

    Command command = Command.fromString(commandText);

    if (command == null) {
      botService.sendMessage(chatId, promptsService.get("bot.unknown.command", locale), bot);
      return;
    }

    switch (command) {
      case START -> handleStart(chatId, locale, bot);
      case NEW -> {
        userService.prepareForNewParameters(chatId, locale);
        botService.sendMessage(chatId, promptsService.get("bot.enter.parameters", locale), bot);
      }
      case RESET -> {
        userService.resetUserParameters(chatId);
        botService.sendMessage(chatId, promptsService.get("bot.enter.parameters", locale), bot);
        handleStart(chatId, locale, bot);
      }
      case OLD -> botService.sendMessage(chatId, promptsService.get("bot.use.old", locale), bot);
      case REPORT -> reportService.sendReport(chatId, bot, locale);
      case STOP -> {
        botService.sendMessage(chatId, promptsService.get("bot.tracking.stopped", locale), bot);
        handleStart(chatId, locale, bot);
      }
      default ->
          botService.sendMessage(chatId, promptsService.get("bot.unknown.command", locale), bot);
    }
  }

  private void handleStart(Long chatId, Locale locale, DietTelegramBot bot) {
    boolean hasParams = userRepo.findById(chatId).isPresent();
    ReplyKeyboardMarkup keyboard;

    if (hasParams) {
      keyboard =
          KeyboardHandler.builder()
              .addButton(Command.NEW)
              .addButton(Command.RESET)
              .newRow()
              .addButton(Command.OLD)
              .addButton(Command.REPORT)
              .addButton(Command.STOP)
              .build();
    } else {
      keyboard = KeyboardHandler.builder().addButton(Command.NEW).build();
    }

    botService.sendKeyboard(chatId, promptsService.get("bot.select.option", locale), bot, keyboard);
  }

  private Locale getUserLocale(Update update) {
    String lang = update.getMessage().getFrom().getLanguageCode();
    return ("ru".equals(lang)) ? Locale.of("ru") : Locale.ENGLISH;
  }

  private void updateKeyboardAfterInput(Long chatId, Locale locale, DietTelegramBot bot) {
    ReplyKeyboardMarkup keyboard =
        KeyboardHandler.builder()
            .addButton(Command.REPORT)
            .addButton(Command.RESET)
            .newRow()
            .addButton(Command.STOP)
            .build();

    botService.sendKeyboard(chatId, promptsService.get("bot.select.option", locale), bot, keyboard);
  }
}
