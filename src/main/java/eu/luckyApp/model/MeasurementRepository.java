package eu.luckyApp.model;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

/*	@Query("SELECT m FROM Measurement m WHERE m.server.id=:serverId AND m.date BETWEEN :startDate AND :endDate")
*/	@Query("SELECT m FROM Measurement m WHERE m.date BETWEEN :startDate AND :endDate AND MOD(m.id,:modulo)=0")
	public Iterable<Measurement> findAllFromServerByDates( @Param("startDate") Date startDate,
			@Param("endDate") Date endDate,@Param("modulo")int modulo);

	//@Query("SELECT m FROM Measurement m WHERE m.server.id=:serverId AND m.date >=:startDate")
//	@Query("SELECT m FROM Measurement m WHERE   m.date >=:startDate")//AND MOD(m.id,10)=0
	//public Iterable<Measurement> findAllFromServerByStartDate( @Param("startDate") Date startDate);
	@Query("SELECT m FROM Measurement m WHERE   m.date >=:startDate AND MOD(m.id,:modulo)=0")
	public Iterable<Measurement> findAllFromServerByStartDate( @Param("startDate") Date startDate,@Param("modulo")int modulo);
	
	

	//@Query("SELECT m FROM Measurement m WHERE m.server.id=:serverId")
	@Query("SELECT m FROM Measurement m ")
	public Iterable<Measurement> findAllFromServer();

	//@Query("SELECT MAX(m.date) FROM Measurement m WHERE m.server.id=:serverId")
	@Query("SELECT MAX(m.date) FROM Measurement m ")
	public Date findLastMeasurementDate();

	//@Query("SELECT MAX(m.id) FROM Measurement m WHERE m.server.id=:serverId")
	@Query("SELECT MAX(m.id) FROM Measurement m ")
	public Long findLastMeasurementIdByServer();

	@Modifying
	@Query(nativeQuery=true,value="DELETE FROM Value")
	public void deleteAllValues();
	
	/*@Modifying
	@Query(nativeQuery=true,value="DELETE FROM MEASUREMENT;")
	public void deleteAllMeasurements();*/
	
	//@Query(nativeQuery=true,value="SELECT * FROM Measurement WHERE MINUTE(date)=0 AND serverId=:serverId")

	//@Query(nativeQuery=true,value="SELECT * FROM MEASUREMENT  where  MINUTE(DATE)=0  AND measurement.server_id=:serverId")//MEASUREMENT.ID, MEASUREMENT.DATE, MEASUREMENT.SERVER_ID, measured_value  LEFT JOIN VALUE  ON Measurement.ID=value.id 
	@Query(nativeQuery=true,value="SELECT * FROM MEASUREMENT  where  MINUTE(DATE)=0 ")//MEASUREMENT.ID, MEASUREMENT.DATE, MEASUREMENT.SERVER_ID, measured_value  LEFT JOIN VALUE  ON Measurement.ID=value.id 
	public Iterable<Measurement>findAllInEveryHour();
	
	@Query("SELECT m FROM Measurement m WHERE  m.date<:date")
	public Iterable<Measurement>findOlderThan(@Param("date")Date date);
}
