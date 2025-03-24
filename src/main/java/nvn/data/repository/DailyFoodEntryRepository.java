package nvn.data.repository;

import java.time.LocalDate;
import java.util.List;

import nvn.data.entity.DailyFoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyFoodEntryRepository extends JpaRepository<DailyFoodEntry, Long> {
  List<DailyFoodEntry> findByTelegramUserIdAndDate(Long telegramUserId, LocalDate date);
}
