package de.uni.bonn.iai.eis;

import de.uni.bonn.iai.eis.web.model.CodelistTransformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodelistTransformationRepository extends JpaRepository<CodelistTransformation, String> {
}
