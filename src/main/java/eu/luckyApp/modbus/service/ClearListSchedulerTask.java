package eu.luckyApp.modbus.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.luckyApp.events.SchedulerEvent;

//@Service
public class ClearListSchedulerTask implements ApplicationEventPublisherAware {
	
	private ApplicationEventPublisher publisher;
	private static final Logger LOG = Logger.getLogger(ClearListSchedulerTask.class);

	@Scheduled(cron="0/20 * * * * ?")
	public void newClerList() {
		LOG.info("0 0/0 6/8 * * ?" + new Date());
		publisher.publishEvent(new SchedulerEvent(this));

	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher appPublisher) {
		this.publisher=appPublisher;
	}

}
