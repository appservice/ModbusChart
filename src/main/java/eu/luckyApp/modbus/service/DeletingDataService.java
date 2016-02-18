package eu.luckyApp.modbus.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.luckyApp.model.FilePathEntity;
import eu.luckyApp.model.Measurement;
import eu.luckyApp.repository.FilePathRepository;
import eu.luckyApp.repository.MeasurementRepository;


/**
 * 
 * @author luke
 * This is service which delete all data older than 1 year in every midnight
 *
 */
@Service
public class DeletingDataService {
	private static final Logger LOG=Logger.getLogger(DeletingDataService.class);
	
	//@Autowired
	MeasurementRepository mRepository;
	
	@Autowired
	FilePathRepository fRepository;
	
	@Value(value="${deleteservice.daynumbers}")
	int dayNumbers;
	
	@Value(value="${deleteservice.deletefiles}")
	boolean deleteFiles;
	
	

	@SuppressWarnings("rawtypes")
	public void deleteMeasurementsOlderThan(Date date){		
	Iterable<Measurement> measurements=mRepository.findOlderThan(date);
		//LOG.info();
		int count=0;
		if(measurements instanceof Collection){
			count =((Collection)measurements).size();
		}
	
		mRepository.delete(measurements);
		LOG.info("Deleted older than year: "+count+" items");
		
	}
	

	@Scheduled(cron="${deleteservice.cronstetment}")
	@Transactional
	public void deleteOlderThanYear(){
		Date nDaysAgoDate = calculateDateOlderThan(dayNumbers);
		//deleteMeasurementsOlderThan(nDaysAgoDate);
		if(deleteFiles){
		deleteFilePathsOlderThan(nDaysAgoDate);
		}
		
	}


	private void deleteFilePathsOlderThan(Date nDaysAgoDate) {
		LOG.info("delete file service run");
		Iterable<FilePathEntity> filePathes=fRepository.findOlderThan(nDaysAgoDate);
		/*int count=0;
		if(filePathes instanceof Collection){
			count =((Collection)filePathes).size();
		}*/
		for(FilePathEntity fpe:filePathes){
			java.nio.file.Path file=Paths.get(fpe.getAbsolutePath());
			try {
				Files.deleteIfExists(file);
				fRepository.delete(fpe);
				LOG.info("REMOVED FILE : "+fpe.getAbsolutePath() );
			} catch (IOException e) {
				
				LOG.error(e);
			}
			
		}
		
		// fRepository.delete(filePathes);
		
		
	}


	private Date calculateDateOlderThan(int dayNumbers) {
		Calendar today=Calendar.getInstance();
		//one year later
		today.add(Calendar.DAY_OF_YEAR, -dayNumbers);		
		Date calculatedDate=today.getTime();
		return calculatedDate;
	}
	
}
