package nvn.models.prompt;

public class AiPrompts {

  public static final String GENERATE_NUTRITION_PLAN =
      """
          You are a nutrition expert. Generate a personalized nutrition plan in {language} language based on the user parameters provided below:

          {parameters}

          """;

  public static final String ANALYZE_FOOD_TEXT =
      """
          Analyze the following food description provided by the user in {language} language:

          "{foodDescription}"

          Estimate calories, protein (g), fat (g), and carbs (g).
          """;

  public static final String ANALYZE_FOOD_PHOTO =
      """
          Analyze the food depicted in this image in {language} language. Estimate calories, protein (g), fat (g), and carbs (g).
          Estimate the volume or quantity to calculate calories accurately and add commentary to field 'commentary' what you think about this position

          """;

  public static final String ANALYZE_FOOD_PHOTO_WITH_COMMENT =
      """
          Analyze the food depicted in this image in {language} language, considering the user's comment:

          "{userCommentary}"

          If the user comment doesn't specify quantity, estimate volume/weight yourself to calculate calories accurately.

          """;
}
