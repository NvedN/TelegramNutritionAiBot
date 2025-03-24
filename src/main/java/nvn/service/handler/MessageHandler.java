package nvn.service.handler;

import java.time.LocalDate;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nvn.bot.KeyboardHandler;
import nvn.service.TelegramBotService;
import nvn.bot.DietTelegramBot;
import nvn.data.entity.DailyFoodEntry;
import nvn.data.repository.DailyFoodEntryRepository;
import nvn.data.repository.UserParametersRepository;
import nvn.models.dto.DailyFoodEntryDto;
import nvn.models.dto.NutritionPlanDto;
import nvn.models.enums.Command;
import nvn.models.enums.UserState;
import nvn.service.AiService;
import nvn.service.PromptsService;
import nvn.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageHandler {

  private final AiService aiService;
  private final DailyFoodEntryRepository foodRepo;
  private final TelegramBotService botService;
  private final PromptsService promptsService;
  private final UserParametersRepository userRepo;
  private final UserService userService;

  public void handle(Update update, DietTelegramBot bot) {
    Long chatId = update.getMessage().getChatId();
    Locale locale = getUserLocale(update);

    userRepo
        .findById(chatId)
        .ifPresentOrElse(
            user -> {
              if (user.getState() == UserState.AWAITING_PARAMETERS
                  && update.getMessage().hasText()) {
                handleUserParameters(chatId, update.getMessage().getText(), locale, bot);
              } else if (update.getMessage().hasPhoto()) {
                handlePhoto(update, chatId, locale, bot);
              } else if (update.getMessage().hasText()) {
                handleFoodTextEntry(chatId, update.getMessage().getText(), locale, bot);
              } else {
                botService.sendMessage(
                    chatId, promptsService.get("bot.unknown.command", locale), bot);
              }
            },
            () ->
                botService.sendMessage(chatId, promptsService.get("bot.start.first", locale), bot));
  }

  private void handleUserParameters(Long chatId, String text, Locale locale, DietTelegramBot bot) {
    try {
      double weight = parseWeight(text);
      double height = parseHeight(text);

      NutritionPlanDto planDto = aiService.generateNutritionPlan(text, locale);
      userService.saveUserParametersAndPlan(chatId, text, planDto, weight, height, locale);

      botService.sendMessage(chatId, planDto.toLocalizedString(locale, promptsService), bot);
      updateKeyboardAfterInput(chatId, locale, bot);
    } catch (Exception e) {
      log.error("Error handling user parameters", e);
      botService.sendMessage(chatId, promptsService.get("bot.analysis.error", locale), bot);
    }
  }

  private void handlePhoto(Update update, Long chatId, Locale locale, DietTelegramBot bot) {
    try {
      String fileId = update.getMessage().getPhoto().getLast().getFileId();
      String photoBase64 = botService.downloadPhotoAsBase64(fileId, bot);

      String userComment = update.getMessage().getCaption();
      DailyFoodEntryDto foodEntryDto =
          (userComment == null || userComment.isBlank())
              ? aiService.analyzePhoto(photoBase64, locale)
              : aiService.analyzePhotoWithUserPrompt(photoBase64, userComment, locale);

      saveDailyFoodEntry(chatId, foodEntryDto);
      botService.sendMessage(chatId, formatFoodEntry(foodEntryDto, locale), bot);
    } catch (Exception e) {
      log.error("Error handling photo input", e);
      botService.sendMessage(chatId, promptsService.get("bot.analysis.error", locale), bot);
    }
  }

  private void handleFoodTextEntry(Long chatId, String text, Locale locale, DietTelegramBot bot) {
    try {
      DailyFoodEntryDto foodEntryDto = aiService.analyzeTextPrompt(text, locale);
      saveDailyFoodEntry(chatId, foodEntryDto);
      botService.sendMessage(chatId, formatFoodEntry(foodEntryDto, locale), bot);
    } catch (Exception e) {
      log.error("Error handling text entry", e);
      botService.sendMessage(chatId, promptsService.get("bot.analysis.error", locale), bot);
    }
  }

  private void saveDailyFoodEntry(Long chatId, DailyFoodEntryDto dto) {
    DailyFoodEntry entry =
        DailyFoodEntry.builder()
            .telegramUserId(chatId)
            .date(LocalDate.now())
            .calories(dto.getCalories())
            .protein(dto.getProtein())
            .fat(dto.getFat())
            .carbs(dto.getCarbs())
            .build();
    foodRepo.save(entry);
  }

  private String formatFoodEntry(DailyFoodEntryDto dto, Locale locale) {
    return promptsService.get("bot.food.entry.saved", locale)
        + "\n"
        + promptsService.get("nutritionPlan.calories", locale, dto.getCalories())
        + "\n"
        + promptsService.get("nutritionPlan.protein", locale, dto.getProtein())
        + "\n"
        + promptsService.get("nutritionPlan.fat", locale, dto.getFat())
        + "\n"
        + promptsService.get("nutritionPlan.carbs", locale, dto.getCarbs())
        + "\n"
        + promptsService.get("nutritionPlan.commentary", locale)
        + dto.getCommentary();
  }

  private double parseWeight(String input) {
    Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\s?(кг|kg)", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(input);
    if (matcher.find()) {
      return Double.parseDouble(matcher.group(1));
    }
    throw new IllegalArgumentException("Вес не найден");
  }

  private double parseHeight(String input) {
    Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\s?(см|cm)", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(input);
    if (matcher.find()) {
      return Double.parseDouble(matcher.group(1));
    }
    throw new IllegalArgumentException("Рост не найден");
  }

  private void updateKeyboardAfterInput(Long chatId, Locale locale, DietTelegramBot bot) {
    botService.sendKeyboard(
        chatId,
        promptsService.get("bot.select.option", locale),
        bot,
        KeyboardHandler.builder()
            .addButton(Command.REPORT)
            .addButton(Command.RESET)
            .newRow()
            .addButton(Command.STOP)
            .build());
  }

  private Locale getUserLocale(Update update) {
    String lang = update.getMessage().getFrom().getLanguageCode();
    return ("ru".equals(lang)) ? Locale.of("ru") : Locale.ENGLISH;
  }
}
