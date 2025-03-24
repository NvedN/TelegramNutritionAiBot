package nvn.data.repository;

import nvn.data.entity.UserParameters;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserParametersRepository extends JpaRepository<UserParameters, Long> {
  void deleteById(Long id);
}
