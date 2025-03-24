package nvn.service;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.function.ToIntFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nvn.data.entity.UserParameters;
import nvn.bot.DietTelegramBot;
import nvn.data.entity.DailyFoodEntry;
import nvn.data.repository.DailyFoodEntryRepository;
import nvn.data.repository.UserParametersRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

  private final DailyFoodEntryRepository foodRepo;
  private final UserParametersRepository userRepo;
  private final PromptsService promptsService;

  public void sendReport(Long chatId, DietTelegramBot bot, Locale locale) {
    LocalDate today = LocalDate.now();

    var entries = foodRepo.findByTelegramUserIdAndDate(chatId, today);
    int totalCalories = sum(entries, DailyFoodEntry::getCalories);
    int totalProtein = sum(entries, DailyFoodEntry::getProtein);
    int totalFat = sum(entries, DailyFoodEntry::getFat);
    int totalCarbs = sum(entries, DailyFoodEntry::getCarbs);

    Optional<UserParameters> userParametersOpt = userRepo.findById(chatId);

    StringBuilder report = new StringBuilder();
    report.append(promptsService.get("bot.report.todaySummary", locale)).append("\n");
    appendMacro(report, "bot.report.calories", totalCalories, locale);
    appendMacro(report, "bot.report.protein", totalProtein, locale);
    appendMacro(report, "bot.report.fat", totalFat, locale);
    appendMacro(report, "bot.report.carbs", totalCarbs, locale);

    userParametersOpt
        .flatMap(u -> Optional.ofNullable(u.getNutritionPlan()))
        .ifPresent(
            nutritionPlan -> {
              report
                  .append("\n")
                  .append(promptsService.get("bot.report.remainingToday", locale))
                  .append("\n");
              appendMacro(
                  report,
                  "bot.report.calories",
                  nutritionPlan.getDailyCalories() - totalCalories,
                  locale);
              appendMacro(
                  report,
                  "bot.report.protein",
                  nutritionPlan.getDailyProtein() - totalProtein,
                  locale);
              appendMacro(report, "bot.report.fat", nutritionPlan.getDailyFat() - totalFat, locale);
              appendMacro(
                  report, "bot.report.carbs", nutritionPlan.getDailyCarbs() - totalCarbs, locale);
            });

    sendMessage(chatId, report.toString(), bot);
  }

  private void appendMacro(StringBuilder report, String key, int value, Locale locale) {
    report.append(promptsService.get(key, locale, value)).append("\n");
  }

  private int sum(Iterable<DailyFoodEntry> entries, ToIntFunction<DailyFoodEntry> mapper) {
    int total = 0;
    for (var entry : entries) {
      total += mapper.applyAsInt(entry);
    }
    return total;
  }

  private void sendMessage(Long chatId, String message, DietTelegramBot bot) {
    try {
      bot.execute(new SendMessage(chatId.toString(), message));
    } catch (TelegramApiException e) {
      log.error("Error sending report to chatId={}", chatId, e);
    }
  }
}
