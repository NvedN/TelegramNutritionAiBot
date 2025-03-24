package nvn.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Locale;
import lombok.Data;
import nvn.service.PromptsService;

@Data
public class NutritionPlanDto {
  @JsonProperty("calories")
  private int calories;

  @JsonProperty("protein")
  private int protein;

  @JsonProperty("fat")
  private int fat;

  @JsonProperty("carbs")
  private int carbs;

  @JsonProperty("recommendation")
  private String description;

  public String toLocalizedString(Locale locale, PromptsService promptsService) {
    return promptsService.get("nutritionPlan.calories", locale, calories)
        + "\n"
        + promptsService.get("nutritionPlan.protein", locale, protein)
        + "\n"
        + promptsService.get("nutritionPlan.fat", locale, fat)
        + "\n"
        + promptsService.get("nutritionPlan.carbs", locale, carbs)
        + "\n\n"
        + description;
  }
}
