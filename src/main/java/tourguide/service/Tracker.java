package tourguide.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import tourguide.configuration.CustomProperties;
import tourguide.modele.User;

public class Tracker extends Thread {
	
	@Autowired
	private CustomProperties props;
	
	private Logger logger = LoggerFactory.getLogger(Tracker.class);
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final TourguideService tourguideService;
	private boolean stop = false;

	public Tracker(TourguideService tourguideService) {
		System.out.println("---entr�e dans Tracker");
		this.tourguideService = tourguideService;
		executorService.submit(this);
	}
	
	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		stop = true;
		System.out.println("fin tracking");
		executorService.shutdownNow();
	}
	
	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while(true) {
			if(Thread.currentThread().isInterrupted() || stop) {
				logger.debug("Tracker stopping");
				break;
			}
			List<User> users = tourguideService.getAllUsers();
			logger.debug("Begin Tracker. Tracking " + users.size() + " users."); 
			stopWatch.start();
			users.forEach(u -> tourguideService.trackUserLocation(u));
			stopWatch.stop();
			logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
			stopWatch.reset();
			try {
				logger.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(props.getTrackingPollingInterval()); //props.getTrackingPollingInterval()
			} catch (InterruptedException e) {
				break;
			}
		}
		
	}
}
