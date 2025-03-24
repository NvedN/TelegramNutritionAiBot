package nvn.data.entity;

import jakarta.persistence.*;
import lombok.*;
import nvn.models.enums.UserState;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
@ToString
public class UserParameters {
  @Id private Long id;
  private double weight;
  private double height;
  private String dailyActivity;

  @Enumerated(EnumType.STRING)
  private UserState state;

  @OneToOne(
      mappedBy = "userParameters",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private NutritionPlan nutritionPlan;

  private String locale;
}
