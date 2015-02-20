package eu.luckyApp.model;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends CrudRepository<Measurement, Long> {
	
	
	@Query("SELECT m FROM Measurement m WHERE m.date BETWEEN :beginDate AND :endDate")
	public Iterable<Measurement> findAll(@Param("beginDate")Date beginDate, @Param("endDate") Date endDate);
	
	//public Iterable<>

}
