package eu.luckyApp.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;

import eu.luckyApp.events.MeasureEvent;
import eu.luckyApp.model.Measurement;

//@Component
//@Path("/")
public class FlowMeasurementRS implements ApplicationListener<MeasureEvent> {

	Logger LOG = Logger.getLogger(FlowMeasurementRS.class);

	private List<Measurement> mesList = new ArrayList<>();

	private Measurement tempMes;// =new Measurement();

	//
	@Override
	public void onApplicationEvent(MeasureEvent evt) {
		if (this.tempMes == null) {
			this.tempMes = (Measurement) evt.getSource();

		} else {

			tempMes.add((Measurement) evt.getSource()); //3600000/(serverRepository.findOne(1L)).getTimeInterval()
			mesList.add(calculatePerHour(tempMes.deepClone(),3600.0));

		}
		LOG.debug(tempMes);

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Measurement> getAll() {
		// return null;
		return mesList;
	}
	
	
//	@Scheduled(cron="0 0/30 9/8 * * ?")
	public void clearMeasurementList(){
		
		LOG.info("*/20 * * * * ? + "+new Date());
		mesList.clear();
		
		
	}
	
	private Measurement calculatePerHour(Measurement m,Double divisor){
		Measurement measurement=new Measurement();
		measurement.setDate(m.getDate());
		
		for(Double value:m.getMeasuredValue()){
			measurement.getMeasuredValue().add(value/divisor);
		}
		return measurement;
	}

}
