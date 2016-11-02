package de.uni.bonn.iai.eis;

import de.uni.bonn.iai.eis.web.model.DataTransformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataTransformationRepository extends JpaRepository<DataTransformation, String> {

}
