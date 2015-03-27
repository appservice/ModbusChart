package eu.luckyApp.modbus.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.luckyApp.model.Measurement;
import eu.luckyApp.model.MeasurementRepository;


/**
 * 
 * @author luke
 * This is service which delete all data older than 1 year in every midnight
 *
 */
@Service
public class DeletingDataService {
	private static final Logger LOG=Logger.getLogger(DeletingDataService.class);
	
	@Autowired
	MeasurementRepository mRepository;
	
	@Value(value="${deleteservice.daynumbers}")
	int dayNumbers;
	
	

	@SuppressWarnings("rawtypes")
	public void deleteOlderThan(Date date){		
	Iterable<Measurement> measurements=mRepository.findOlderThan(date);
		//LOG.info();
		int count=0;
		if(measurements instanceof Collection){
			count =((Collection)measurements).size();
		}
	
		mRepository.delete(measurements);
		LOG.info("Deleted older than year: "+count+" items");
		
	}
	

	@Scheduled(cron="0 0 23 * * ?")
	@Transactional
	public void deleteOlderThanYear(){
		Calendar today=Calendar.getInstance();


		//one year later
		today.add(Calendar.DAY_OF_YEAR, -dayNumbers);
		
		Date yearAgoDate=today.getTime();
		deleteOlderThan(yearAgoDate);
		
		
	}
	
}
