package tourguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.junit.Test; // JUnit 4
//import org.junit.jupiter.api.Test // JUnit 5
import org.springframework.boot.test.context.SpringBootTest;

import tourguide.configuration.CustomProperties;
import tourguide.modele.Attraction;
import tourguide.modele.Location;
import tourguide.modele.Provider;
import tourguide.modele.TouristAttraction;
import tourguide.modele.User;
import tourguide.modele.UserReward;
import tourguide.modele.VisitedLocation;
import tourguide.repository.GpsUtilProxy;
import tourguide.repository.RewardsProxy;
import tourguide.repository.TripPricerProxy;
import tourguide.service.TourguideService;

@SpringBootTest
public class TestTourGuideService {

	@Autowired
	GpsUtilProxy gpsUtilProxy;
	@Autowired
	RewardsProxy rewardsProxy;
	@Autowired
	TripPricerProxy tripPricerProxy;
	@Autowired
	CustomProperties props;

	@Test
	public void getUserLocation() {
		// GIVEN
		props.setTestMode(true);
		props.setInternalUserNumber(0);
		props.setApiUrlGpsUtil("http://localhost:9001");
		props.setApiUrlRewards("http://localhost:9002");
		props.setDefaultProximityBuffer(10);
		props.setSTATUTE_MILES_PER_NAUTICAL_MILE(1.15077945);
		TourguideService tourguideService = new TourguideService(gpsUtilProxy, rewardsProxy, tripPricerProxy, props);
		tourguideService.getTracker().stopTracking();

		// WHEN
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourguide.com");
		VisitedLocation visitedLocation = tourguideService.trackUserLocation(user);

		// THEN
		assertTrue(visitedLocation.getUserId().equals(user.getUserId()));
	}

	@Test
	public void addUser() {
		// GIVEN
		props.setTestMode(true);
		props.setInternalUserNumber(0);
		props.setApiUrlGpsUtil("http://localhost:9001");
		props.setApiUrlRewards("http://localhost:9002");
		TourguideService tourguideService = new TourguideService(gpsUtilProxy, rewardsProxy, tripPricerProxy, props);
		tourguideService.getTracker().stopTracking();

		// WHEN
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourguide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourguide.com");
		tourguideService.addUser(user);
		tourguideService.addUser(user2);

		// THEN
		assertEquals(tourguideService.getAllUsers().size(), 2);
	}

	@Test
	public void getAllUsers() {
		// GIVEN
		props.setTestMode(true);
		props.setInternalUserNumber(0);
		props.setApiUrlGpsUtil("http://localhost:9001");
		props.setApiUrlRewards("http://localhost:9002");
		TourguideService tourguideService = new TourguideService(gpsUtilProxy, rewardsProxy, tripPricerProxy, props);
		tourguideService.getTracker().stopTracking();

		// WHEN
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourguide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourguide.com");
		tourguideService.addUser(user);
		tourguideService.addUser(user2);
		List<User> allUsers = tourguideService.getAllUsers();

		// THEN
		assertTrue(allUsers.stream().map(x -> x.getUserName()).collect(Collectors.toList()).contains("jon"));
		assertTrue(allUsers.stream().map(x -> x.getUserName()).collect(Collectors.toList()).contains("jon2"));
	}

	@Test
	public void trackUser() {
		// GIVEN
		props.setTestMode(true);
		props.setInternalUserNumber(0);
		props.setApiUrlGpsUtil("http://localhost:9001");
		props.setApiUrlRewards("http://localhost:9002");
		TourguideService tourguideService = new TourguideService(gpsUtilProxy, rewardsProxy, tripPricerProxy, props);
		tourguideService.getTracker().stopTracking();

		// WHEN
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourguide.com");
		VisitedLocation visitedLocation = tourguideService.trackUserLocation(user);

		// THEN
		assertEquals(user.getUserId(), visitedLocation.getUserId());
	}

	@Test
	public void getNearbyAttractions() {
		// GIVEN
		props.setTestMode(true);
		props.setInternalUserNumber(1);
		props.setApiUrlGpsUtil("http://localhost:9001");
		props.setApiUrlRewards("http://localhost:9002");
		props.setSTATUTE_MILES_PER_NAUTICAL_MILE(1.15077945);
		props.setDefaultProximityBuffer(10);
		TourguideService tourguideService = new TourguideService(gpsUtilProxy, rewardsProxy, tripPricerProxy, props);
		tourguideService.getTracker().stopTracking();

		// WHEN
		tourguideService.trackUserLocation(tourguideService.getAllUsers().get(0));
		List<TouristAttraction> touristAttractions = tourguideService
				.getNearByAttractions(tourguideService.getAllUsers().get(0).getUserName());

		// THEN
		assertEquals(5, touristAttractions.size());
	}

	@Test
	public void getTripDeals() {
		// GIVEN
		props.setTestMode(true);
		props.setInternalUserNumber(1);
		props.setApiUrlGpsUtil("http://localhost:9001");
		props.setApiUrlRewards("http://localhost:9002");
		props.setApiUrlTripPricer("http://localhost:9003");
		TourguideService tourguideService = new TourguideService(gpsUtilProxy, rewardsProxy, tripPricerProxy, props);
		tourguideService.getTracker().stopTracking();

		// WHEN
		List<Provider> providers = tourguideService.getTripDeals("internalUser0", 1, 1, 1);

		// THEN
		assertEquals(5, providers.size());

	}

	@Test
	public void userGetRewards() {
		// GIVEN
		props.setTestMode(true);
		props.setInternalUserNumber(0);
		props.setApiUrlGpsUtil("http://localhost:9001");
		props.setApiUrlRewards("http://localhost:9002");
		props.setApiUrlTripPricer("http://localhost:9003");
		props.setDefaultProximityBuffer(10);
		props.setSTATUTE_MILES_PER_NAUTICAL_MILE(1.15077945);
		TourguideService tourguideService = new TourguideService(gpsUtilProxy, rewardsProxy, tripPricerProxy, props);
		tourguideService.getTracker().stopTracking();

		// WHEN
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourguide.com");
		Attraction attraction = gpsUtilProxy.getAttractions(props).get(0);
		VisitedLocation newVisitedLocation = new VisitedLocation(user.getUserId(),
				new Location(attraction.getLongitude(), attraction.getLatitude()), new Date());
		user.addToVisitedLocations(newVisitedLocation);
		tourguideService.calculateRewards(user);
		List<UserReward> userRewards = user.getUserRewards();

		// THEN
		assertEquals(userRewards.size(), 1);
	}

	@Test
	public void isWithinAttractionProximity() {
		// GIVEN
		props.setTestMode(true);
		props.setInternalUserNumber(0);
		props.setApiUrlGpsUtil("http://localhost:9001");
		props.setApiUrlRewards("http://localhost:9002");
		TourguideService tourguideService = new TourguideService(gpsUtilProxy, rewardsProxy, tripPricerProxy, props);
		tourguideService.getTracker().stopTracking();

		// WHEN
		Attraction attraction = gpsUtilProxy.getAttractions(props).get(0);
		Location newLocation = new Location(attraction.getLongitude(), attraction.getLatitude());

		// THEN
		assertTrue(tourguideService.isWithinAttractionProximity(attraction, newLocation));
	}

	@Test
	public void nearAllAttractions() {
		// GIVEN
		props.setTestMode(true);
		props.setInternalUserNumber(1);
		props.setApiUrlGpsUtil("http://localhost:9001");
		props.setApiUrlRewards("http://localhost:9002");
		props.setApiUrlTripPricer("http://localhost:9003");
		props.setDefaultProximityBuffer(Integer.MAX_VALUE);
		props.setSTATUTE_MILES_PER_NAUTICAL_MILE(1.15077945);
		TourguideService tourguideService = new TourguideService(gpsUtilProxy, rewardsProxy, tripPricerProxy, props);
		tourguideService.getTracker().stopTracking();

		// WHEN
		tourguideService.calculateRewards(tourguideService.getAllUsers().get(0));
		List<UserReward> userRewards = tourguideService.getUserRewards(tourguideService.getAllUsers().get(0));

		// THEN
		assertEquals(tourguideService.getAttractions().size(), userRewards.size());
	}

}
