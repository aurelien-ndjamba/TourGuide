package tourguide;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tourguide.configuration.CustomProperties;
import tourguide.modele.Attraction;
import tourguide.modele.Location;
import tourguide.modele.User;
import tourguide.modele.VisitedLocation;
import tourguide.repository.GpsUtilProxy;
import tourguide.repository.RewardsProxy;
import tourguide.repository.TripPricerProxy;
import tourguide.service.TourguideService;

@SpringBootTest
public class TestPerformance {

	/*
	 * A note on performance improvements:
	 * 
	 * The number of users generated for the high volume tests can be easily
	 * adjusted via this method:
	 * 
	 * InternalTestHelper.setInternalUserNumber(100000);
	 * 
	 * 
	 * These tests can be modified to suit new solutions, just as long as the
	 * performance metrics at the end of the tests remains consistent.
	 * 
	 * These are performance metrics that we are trying to hit:
	 * 
	 * highVolumeTrackLocation: 100,000 users within 15 minutes:
	 * assertTrue(TimeUnit.MINUTES.toSeconds(15) >=
	 * TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 *
	 * highVolumeGetRewards: 100,000 users within 20 minutes:
	 * assertTrue(TimeUnit.MINUTES.toSeconds(20) >=
	 * TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */
	@Autowired
	GpsUtilProxy gpsUtilProxy;
	@Autowired
	RewardsProxy rewardsProxy;
	@Autowired
	TripPricerProxy tripPricerProxy;
	@Autowired
	CustomProperties props;

	@BeforeEach
	public void init() {
//		CustomProperties props = new CustomProperties();
//		GpsUtilProxy GpsUtilProxy = new GpsUtilProxy();
//		RewardsProxy rewardsProxy = new RewardsProxy();
//		TripPricerProxy tripPricerProxy = new TripPricerProxy();
		props.setTestMode(true);
		props.setInternalUserNumber(100000);
		props.setApiUrlGpsUtil("http://localhost:9001");
		props.setApiUrlRewards("http://localhost:9002");
		props.setApiUrlTripPricer("http://localhost:9003");
		props.setDefaultProximityBuffer(10);
		props.setTrackingPollingInterval(10);
		props.setSTATUTE_MILES_PER_NAUTICAL_MILE(1.15077945);

	}

	@Test
	public void highVolumeTrackLocation() {
		// GIVEN
//		CustomProperties props = new CustomProperties();
//		GpsUtilProxy GpsUtilProxy = new GpsUtilProxy();
//		RewardsProxy rewardsProxy = new RewardsProxy();
//		TripPricerProxy tripPricerProxy = new TripPricerProxy();
//		// Users should be incremented up to 100,000, and test finishes within 15
//		// minutes
//		props.setTestMode(true);
//		props.setInternalUserNumber(100000);
//		props.setApiUrlGpsUtil("http://localhost:9001");
//		props.setApiUrlRewards("http://localhost:9002");
//		props.setApiUrlTripPricer("http://localhost:9003");
//		props.setDefaultProximityBuffer(10);
//		props.setSTATUTE_MILES_PER_NAUTICAL_MILE(1.15077945);

		TourguideService tourguideService = new TourguideService(gpsUtilProxy, rewardsProxy, tripPricerProxy, props);
		tourguideService.getTracker().stopTracking();

		List<User> users = tourguideService.getAllUsers();

		// WHEN
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		tourguideService.trackUserLocation(users);

		stopWatch.stop();
		tourguideService.tracker.stopTracking();

		// THEN
		System.out.println("highVolumeTrackLocation: Time Elapsed: "
				+ TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	@Test
	public void highVolumeGetRewards() throws InterruptedException {
		// GIVEN
		TourguideService tourguideService = new TourguideService(gpsUtilProxy, rewardsProxy, tripPricerProxy, props);
		tourguideService.getTracker().stopTracking();

		Attraction attraction = gpsUtilProxy.getAttractions(props).get(0);
		List<User> users = tourguideService.getAllUsers();
		users.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(),
				new Location(attraction.getLongitude(), attraction.getLatitude()), new Date())));

		// WHEN
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		tourguideService.calculateRewards(users);

		while (!tourguideService.executorService.isTerminated()) {
			TimeUnit.SECONDS.sleep(props.getTrackingPollingInterval());
		}

		for (User user : users) {
			assertTrue(user.getUserRewards().size() > 0);
		}

		stopWatch.stop();

		// THEN
		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
				+ " seconds.");

		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

}
