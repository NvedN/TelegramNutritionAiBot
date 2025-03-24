package nvn.service.impl;

import java.util.Locale;

import nvn.models.dto.DailyFoodEntryDto;
import nvn.models.dto.NutritionPlanDto;
import nvn.service.AiService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("DEV")
public class AiServiceSTUB implements AiService {
  @Override
  public NutritionPlanDto generateNutritionPlan(String prompt, Locale locale) {
    return new NutritionPlanDto();
  }

  @Override
  public DailyFoodEntryDto analyzeTextPrompt(String prompt, Locale locale) {
    return new DailyFoodEntryDto();
  }

  @Override
  public DailyFoodEntryDto analyzePhoto(String base64Image, Locale locale) {
    return new DailyFoodEntryDto();
  }

  @Override
  public DailyFoodEntryDto analyzePhotoWithUserPrompt(
      String base64Image, String prompt, Locale locale) {
    return new DailyFoodEntryDto();
  }
}
