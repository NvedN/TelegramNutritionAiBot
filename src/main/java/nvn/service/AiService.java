package nvn.service;

import java.util.Locale;

import nvn.models.dto.DailyFoodEntryDto;
import nvn.models.dto.NutritionPlanDto;

public interface AiService {

  NutritionPlanDto generateNutritionPlan(String prompt, Locale locale);

  DailyFoodEntryDto analyzeTextPrompt(String prompt, Locale locale);

  DailyFoodEntryDto analyzePhoto(String base64Image, Locale locale);

  DailyFoodEntryDto analyzePhotoWithUserPrompt(String base64Image, String prompt, Locale locale);
}
