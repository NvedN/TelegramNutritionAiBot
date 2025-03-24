package nvn.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "nutrition_plan",
    uniqueConstraints = {@UniqueConstraint(columnNames = "user_parameters_id")})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class NutritionPlan {
  @Id @GeneratedValue private Long id;
  private int dailyCalories;
  private int dailyProtein;
  private int dailyFat;

  private int dailyCarbs;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_parameters_id", unique = true)
  private UserParameters userParameters;

  @Column(columnDefinition = "TEXT")
  private String description;
}
