package tourguide;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tourguide.configuration.CustomProperties;
import tourguide.modele.User;
import tourguide.repository.GpsUtilProxy;
import tourguide.repository.RewardsProxy;
import tourguide.service.TourguideService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPerformance {
	
	@Autowired
	CustomProperties props;
	@Autowired
	TourguideService TourguideService;
	@Autowired
	GpsUtilProxy gpsUtilProxy;
	@Autowired
	RewardsProxy rewardsProxy;

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

	@Test
	public void highVolumeTrackLocation() {
		// GIVEN
		System.out.println("---?0----" + TourguideService.getAllUsers().size());


		// Users should be incremented up to 100,000, and test finishes within 15
		// minutes
		props.setInternalUserNumber(10);
		TourguideService.initializeInternalUsers();
		List<User> allUsers = new ArrayList<>();
		System.out.println("---100000?----" + TourguideService.getAllUsers().size());
//		InternalTestHelper.setInternalUserNumber(100000);
		

		allUsers = TourguideService.getAllUsers();
		System.out.println(allUsers.size());

		// WHEN 
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (User user : allUsers) {
			gpsUtilProxy.getUserLocation(user.getUserId());
//			TourguideService.trackUserLocation(user);
		}
		stopWatch.stop();
//		TourguideService.tracker.stopTracking();

		System.out.println("highVolumeTrackLocation: Time Elapsed: "
				+ TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		
		// THEN
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	@Test
	public void highVolumeGetRewards() {
		// GIVEN
		System.out.println("---?0----" + TourguideService.getAllUsers().size());


		// Users should be incremented up to 100,000, and test finishes within 15
		// minutes
		props.setInternalUserNumber(10);
		TourguideService.initializeInternalUsers();
		List<User> allUsers = new ArrayList<>();
		System.out.println("---100000?----" + TourguideService.getAllUsers().size());
		

		allUsers = TourguideService.getAllUsers();
		System.out.println(allUsers.size());

		UUID attractionId = TourguideService.attractions.get(0).getAttractionId();
		// WHEN 
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (User user : allUsers) {
			rewardsProxy.getAttractionRewardPoints(attractionId, user.getUserId());
		}
		stopWatch.stop();
//		TourguideService.tracker.stopTracking();

		System.out.println("highVolumeTrackGetRewards: Time Elapsed: "
				+ TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		
		// THEN
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

}
