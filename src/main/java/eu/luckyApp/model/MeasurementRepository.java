package eu.luckyApp.model;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MeasurementRepository extends CrudRepository<Measurement, Long> {

	@Query("SELECT m FROM Measurement m WHERE m.server.id=:serverId AND m.date BETWEEN :startDate AND :endDate")
	public Iterable<Measurement> findAllFromServerByDates(@Param("serverId") Long serverId, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);

	@Query("SELECT m FROM Measurement m WHERE m.server.id=:serverId AND m.date >=:startDate")
	public Iterable<Measurement> findAllFromServerByStartDate(@Param("serverId") Long serverId, @Param("startDate") Date startDate);

	@Query("SELECT m FROM Measurement m WHERE m.server.id=:serverId")
	public Iterable<Measurement> findAllFromServer(@Param("serverId") Long serverId);

	@Query("SELECT MAX(m.date) FROM Measurement m WHERE m.server.id=:serverId")
	public Date findLastMeasurementDate(@Param("serverId") Long serverId);

	@Query("SELECT MAX(m.id) FROM Measurement m WHERE m.server.id=:serverId")
	public Long findLastMeasurementIdByServer(@Param("serverId") Long serverId);

	@Modifying
	@Transactional
	@Query(nativeQuery=true,value="DELETE FROM Measurement ")
	public void deleteAll();
	
	//@Query(nativeQuery=true,value="SELECT * FROM Measurement WHERE MINUTE(date)=0 AND serverId=:serverId")

	@Query(nativeQuery=true,value="SELECT * FROM MEASUREMENT  where  MINUTE(DATE)=0  AND measurement.server_id=:serverId")//MEASUREMENT.ID, MEASUREMENT.DATE, MEASUREMENT.SERVER_ID, measured_value  LEFT JOIN VALUE  ON Measurement.ID=value.id 
	public Iterable<Measurement>findAllInEveryHour(@Param("serverId")Long serverId);
	
	@Query("SELECT m FROM Measurement m WHERE  m.date<:date")
	public Iterable<Measurement>findOlderThan(@Param("date")Date date);
}
