package nvn.data.repository;

import nvn.data.entity.NutritionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, Long> {
  void deleteByUserParametersId(Long userParameters_id);
}



