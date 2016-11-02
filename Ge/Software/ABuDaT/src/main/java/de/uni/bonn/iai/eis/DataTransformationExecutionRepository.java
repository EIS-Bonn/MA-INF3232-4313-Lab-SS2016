package de.uni.bonn.iai.eis;

import de.uni.bonn.iai.eis.web.model.Execution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataTransformationExecutionRepository extends JpaRepository<Execution, String> {
}
