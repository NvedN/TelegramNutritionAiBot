package nvn.service.impl;

import java.util.Base64;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import nvn.models.dto.DailyFoodEntryDto;
import nvn.models.prompt.AiPrompts;
import nvn.service.AiService;
import nvn.models.dto.NutritionPlanDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.Media;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("PROM")
@Slf4j
public class OpenAiService implements AiService {

  private final ChatClient chatClient;

  public OpenAiService(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  @Override
  public NutritionPlanDto generateNutritionPlan(String userParameters, Locale locale) {
    String prompt =
        AiPrompts.GENERATE_NUTRITION_PLAN
            .replace("{language}", locale.getLanguage())
            .replace("{parameters}", userParameters);

    NutritionPlanDto plan = chatClient.prompt().user(prompt).call().entity(NutritionPlanDto.class);

    log.info("Generated Nutrition Plan: {}", plan);
    return plan;
  }

  @Override
  public DailyFoodEntryDto analyzeTextPrompt(String promptText, Locale locale) {
    String prompt =
        AiPrompts.ANALYZE_FOOD_TEXT
            .replace("{language}", "English")
            .replace("{foodDescription}", promptText);

    DailyFoodEntryDto entry =
        chatClient.prompt().user(prompt).call().entity(DailyFoodEntryDto.class);

    log.info("Analyzed Food Text: {}", entry);
    return entry;
  }

  @Override
  public DailyFoodEntryDto analyzePhoto(String base64Image, Locale locale) {
    return analyzePhotoWithPrompt(
        base64Image, AiPrompts.ANALYZE_FOOD_PHOTO.replace("{language}", locale.getLanguage()));
  }

  @Override
  public DailyFoodEntryDto analyzePhotoWithUserPrompt(
      String base64Image, String userCommentary, Locale locale) {
    String prompt =
        AiPrompts.ANALYZE_FOOD_PHOTO_WITH_COMMENT
            .replace("{language}", locale.getLanguage())
            .replace("{userCommentary}", userCommentary);

    return analyzePhotoWithPrompt(base64Image, prompt);
  }

  private DailyFoodEntryDto analyzePhotoWithPrompt(String base64Image, String promptText) {
    byte[] imageBytes = Base64.getDecoder().decode(base64Image);
    Media media = Media.builder().mimeType(Media.Format.IMAGE_JPEG).data(imageBytes).build();

    DailyFoodEntryDto entry =
        chatClient
            .prompt()
            .user(userSpec -> userSpec.text(promptText).media(media))
            .call()
            .entity(DailyFoodEntryDto.class);

    log.info("Analyzed Photo Entry: {}", entry);
    return entry;
  }
}
