package nvn.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class DailyFoodEntry {
  @Id @GeneratedValue private Long id;
  private Long telegramUserId;

  private LocalDate date;

  @Column(columnDefinition = "TEXT")
  private String description;

  private String photoUrl;
  private int calories;
  private int protein;
  private int fat;
  private int carbs;
}
