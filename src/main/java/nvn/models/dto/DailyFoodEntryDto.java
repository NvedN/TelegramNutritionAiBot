package nvn.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DailyFoodEntryDto {
  @JsonProperty("calories")
  private int calories;

  @JsonProperty("protein")
  private int protein;

  @JsonProperty("fat")
  private int fat;

  @JsonProperty("carbs")
  private int carbs;

  @JsonProperty("commentary")
  private String commentary;
}
