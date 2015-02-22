package eu.luckyApp.model;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends CrudRepository<Measurement, Long> {

	@Query("SELECT m FROM Measurement m WHERE m.serverId=:serverId AND m.date BETWEEN :startDate AND :endDate")
	public Iterable<Measurement> findAllFromServerByDates(@Param("serverId") Long serverId, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);

	@Query("SELECT m FROM Measurement m WHERE m.serverId=:serverId AND m.date >=:startDate")
	public Iterable<Measurement> findAllFromServerByStartDate(@Param("serverId") Long serverId, @Param("startDate") Date startDate);

	// public Iterable<>
	@Query("SELECT m FROM Measurement m WHERE m.serverId=:serverId")
	public Iterable<Measurement> findAllFromServer(@Param("serverId") Long serverId);
	
	@Query("SELECT MAX(m.date) FROM Measurement m WHERE m.serverId=:serverId" )
	public Date findLastMeasurementDate(@Param("serverId") Long serverId);

}
