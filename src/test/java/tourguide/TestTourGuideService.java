package tourguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tourguide.configuration.CustomProperties;
import tourguide.modele.Attraction;
import tourguide.modele.Location;
import tourguide.modele.Provider;
import tourguide.modele.TouristAttraction;
import tourguide.modele.User;
import tourguide.modele.UserReward;
import tourguide.modele.VisitedLocation;
import tourguide.repository.GpsUtilProxy;
import tourguide.service.TourguideService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTourGuideService {

	@Autowired
	CustomProperties props;
	@Autowired
	TourguideService TourguideService;
	@Autowired
	GpsUtilProxy gpsUtilProxy;

	@Test
	public void contextLoads() {
	}

	@Before
	public void init() {
		props.setInternalUserNumber(0);
		props.setDefaultProximityBuffer(10);
	}

	@Test
	public void getUserLocation() {
		// GIVEN
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourguide.com");

		// WHEN
		VisitedLocation visitedLocation = TourguideService.trackUserLocation(user);
		TourguideService.getTracker().stopTracking();

		// THEN
		assertTrue(visitedLocation.getUserId().equals(user.getUserId()));
	}

	@Test
	public void addUser() {
		// GIVEN
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourguide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourguide.com");

		TourguideService.addUser(user);
		TourguideService.addUser(user2);

		// WHEN
		User retrivedUser = TourguideService.getUser(user.getUserName());
		User retrivedUser2 = TourguideService.getUser(user2.getUserName());

		TourguideService.getTracker().stopTracking();

		// THEN
		assertEquals(user, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}

	@Test
	public void getAllUsers() {
		// GIVEN
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourguide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourguide.com");

		// WHEN
		TourguideService.addUser(user);
		TourguideService.addUser(user2);

		List<User> allUsers = TourguideService.getAllUsers();
		TourguideService.getTracker().stopTracking();

		// THEN
		assertTrue(allUsers.stream().map(x -> x.getUserName()).collect(Collectors.toList()).contains("jon"));
		assertTrue(allUsers.stream().map(x -> x.getUserName()).collect(Collectors.toList()).contains("jon2"));
	}

	@Test
	public void trackUser() {
		// GIVEN
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourguide.com");

		// WHEN
		VisitedLocation visitedLocation = TourguideService.trackUserLocation(user);
		TourguideService.getTracker().stopTracking();

		// THEN
		assertEquals(user.getUserId(), visitedLocation.getUserId());
	}

	@Test
	public void getNearbyAttractions() {
		// GIVEN
		System.out.println(props.getInternalUserNumber() + "<--------");
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourguide.com");

		// WHEN
		TourguideService.addUser(user);
		VisitedLocation visitedLocation = TourguideService.trackUserLocation(user);
		List<TouristAttraction> attractions = TourguideService.getNearByAttractions(visitedLocation);

		TourguideService.getTracker().stopTracking();

		// THEN
		assertEquals(5, attractions.size());
	}

	@Test
	public void getTripDeals() {
		// GIVEN
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourguide.com");

		// WHEN
		List<Provider> providers = TourguideService.getTripDeals(user.getUserName(), 1, 1, 1);

		TourguideService.getTracker().stopTracking();

		// THEN
		assertEquals(5, providers.size());

	}

	@Test
	public void userGetRewards() {
		// GIVEN
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourguide.com");
		Attraction attraction = gpsUtilProxy.getAttractions().get(0);
		VisitedLocation newVisitedLocation = new VisitedLocation(user.getUserId(),
				new Location(attraction.getLongitude(), attraction.getLatitude()), new Date());

		user.addToVisitedLocations(newVisitedLocation);

		// WHEN
		TourguideService.calculateRewards(user);
		List<UserReward> userRewards = user.getUserRewards();
		TourguideService.getTracker().stopTracking();

		// THEN
		assertTrue(userRewards.size() == 1);
	}

	@Test
	public void isWithinAttractionProximity() {
		// GIVEN
		Attraction attraction = gpsUtilProxy.getAttractions().get(0);

		// WHEN
		Location newLocation = new Location(attraction.getLongitude(), attraction.getLatitude());

		// THEN
		assertTrue(TourguideService.isWithinAttractionProximity(attraction, newLocation));
	}

	@Test
	public void nearAllAttractions() {
		// GIVEN
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourguide.com");
		props.setDefaultProximityBuffer(Integer.MAX_VALUE);

		// WHEN
		TourguideService.trackUserLocation(user); 
		List<UserReward> userRewardsAll = TourguideService.getUserRewards(user);
		TourguideService.getTracker().stopTracking();

		// THEN
		assertEquals(gpsUtilProxy.getAttractions().size(), userRewardsAll.size());
	}

}
