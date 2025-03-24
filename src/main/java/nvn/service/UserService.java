package nvn.service;

import jakarta.transaction.Transactional;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nvn.data.entity.NutritionPlan;
import nvn.data.entity.UserParameters;
import nvn.data.repository.NutritionPlanRepository;
import nvn.data.repository.UserParametersRepository;
import nvn.models.dto.NutritionPlanDto;
import nvn.models.enums.UserState;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserParametersRepository userRepo;
  private final NutritionPlanRepository planRepo;

  @Transactional
  public void resetUserParameters(Long telegramUserId) {
    planRepo.deleteByUserParametersId(telegramUserId);
    userRepo.deleteById(telegramUserId);
  }

  @Transactional
  public void prepareForNewParameters(Long telegramUserId, Locale locale) {
    resetUserParameters(telegramUserId);

    UserParameters user =
        UserParameters.builder()
            .id(telegramUserId)
            .state(UserState.AWAITING_PARAMETERS)
            .locale(locale.getLanguage())
            .build();

    userRepo.save(user);
  }

  @Transactional
  public void saveUserParametersAndPlan(
      Long telegramUserId,
      String text,
      NutritionPlanDto planDto,
      double weight,
      double height,
      Locale Locale) {

    UserParameters user =
        userRepo
            .findById(telegramUserId)
            .map(
                existingUser ->
                    existingUser.toBuilder()
                        .weight(weight)
                        .height(height)
                        .dailyActivity(text)
                        .state(UserState.ACTIVE)
                        .build())
            .orElseGet(
                () ->
                    UserParameters.builder()
                        .id(telegramUserId)
                        .weight(weight)
                        .height(height)
                        .dailyActivity(text)
                        .state(UserState.ACTIVE)
                        .locale(Locale.getLanguage())
                        .build());

    NutritionPlan plan =
        NutritionPlan.builder()
            .dailyCalories(planDto.getCalories())
            .dailyProtein(planDto.getProtein())
            .dailyFat(planDto.getFat())
            .dailyCarbs(planDto.getCarbs())
            .description(planDto.getDescription())
            .userParameters(user)
            .build();

    user = user.toBuilder().nutritionPlan(plan).build();

    userRepo.save(user);
  }
}
