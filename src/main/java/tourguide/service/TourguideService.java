package tourguide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;
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

@Data
@Service
public class TourguideService {

	private Logger logger = LoggerFactory.getLogger(TourguideService.class);

	@Autowired
	private CustomProperties props;
	@Autowired
	private GpsUtilProxy gpsUtilProxy;
	@Autowired
	private RewardsProxy rewardsProxy;
	@Autowired
	private TripPricerProxy tripPricerProxy;

	public static Map<String, User> internalUserMap = new HashMap<String, User>(); // final

	public static List<Attraction> attractions = new ArrayList<Attraction>();

	public final Tracker tracker;

	public TourguideService(GpsUtilProxy gpsUtilProxy,
			CustomProperties props) {
		this.gpsUtilProxy = gpsUtilProxy;
		this.props = props;
		System.out.println("internalUserNumber?"+props.getInternalUserNumber());
		System.out.println("testMode?"+props.isTestMode());
		if (props.isTestMode()) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}

	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public VisitedLocation getUserLocation(String userName) {
		User user = getUser(userName);
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
				: trackUserLocation(user);
		return visitedLocation;
	}

	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	public List<Provider> getTripDeals(String userName, int numberOfAdults, int numberOfChildren, int tripDuration) {
		User user = getUser(userName);
		user.getUserPreferences().setNumberOfAdults(numberOfAdults);
		user.getUserPreferences().setNumberOfChildren(numberOfChildren);
		user.getUserPreferences().setTripDuration(tripDuration);
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricerProxy.getPrice(tripPricerApiKey, user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	/*
	 * Track Lieu visit� d'un utilisateur en fonction de son Id & Calcul les points
	 * de voyages
	 */
	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtilProxy.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		calculateRewards(user);
//		VisitedLocation visitedLocation = null; // AJOUT
		return visitedLocation;
	}

	/*
	 * Position de tous les utilisateurs internes
	 */
	public List<Map<UUID, Location>> getAllCurrentLocations() {
		List<Map<UUID, Location>> currentLocations = new ArrayList<>();

		for (User user : internalUserMap.values()) {
			Map<UUID, Location> currentLocation = new HashMap<>();
			currentLocation.put(user.getUserId(), user.getLastVisitedLocation().getLocation());
			currentLocations.add(currentLocation);
		}

		return currentLocations;
	}

	/*
	 * Liste des 5 attractions touristiques les plus proches � proximit� d'un lieux
	 * donn�
	 */
	public List<TouristAttraction> getNearByAttractions(String userName) {
		VisitedLocation visitedLocation = internalUserMap.get(userName).getLastVisitedLocation();
		return getNearByAttractions(visitedLocation);
	}
	
	public List<TouristAttraction> getNearByAttractions(VisitedLocation visitedLocation) {
		
		///////////// METHOD 1 //////////////
		
		props.setDefaultProximityBuffer(Integer.MAX_VALUE); 
		List<TouristAttraction> touristAttractions = new ArrayList<TouristAttraction>();
		List<TouristAttraction> fiveNearestTouristAttraction;
		List<Double> distancesToAttraction = new ArrayList<Double>();
		for (Attraction attraction : attractions) {
			double distance = getDistance(new Location(attraction.getLongitude(), attraction.getLatitude()),
					visitedLocation.getLocation());

			distancesToAttraction.add(distance);
			Collections.sort(distancesToAttraction);

			TouristAttraction touristAttraction = new TouristAttraction();
			touristAttraction.setAttractionName(attraction.getAttractionName());
			touristAttraction.setTouristLocation(new Location(attraction.getLatitude(), attraction.getLongitude()));
			touristAttraction.setUserLocation(visitedLocation.getLocation());
			touristAttraction.setDistance(distance);
			touristAttraction.setRewardPoints(
					rewardsProxy.getAttractionRewardPoints(attraction.getAttractionId(), visitedLocation.getUserId()));

			touristAttractions.add(touristAttraction);
		}

		fiveNearestTouristAttraction = touristAttractions.stream()
				.filter(x -> x.getDistance() < distancesToAttraction.get(5)).collect(Collectors.toList());

		return fiveNearestTouristAttraction;
		
		///////////// METHOD 2 //////////////
		
//		props.setDefaultProximityBuffer(Integer.MAX_VALUE); 
//		List<TouristAttraction> allNearestTouristAttraction =
//		attractions.stream().filter(x -> nearAttraction(visitedLocation, x))
//							.map(x -> new TouristAttraction( 
//									x.getAttractionName(),
//									new Location(x.getLongitude(), x.getLatitude()),
//									visitedLocation.getLocation(),
//									getDistance(new Location(x.getLongitude(), x.getLatitude()), visitedLocation.getLocation()),
//									rewardsProxy.getAttractionRewardPoints(x.getAttractionId(), visitedLocation.getUserId())))
//							.collect(Collectors.toList());
//		
//		List<Double> distancesToAttraction =
//		attractions.stream().filter(x -> nearAttraction(visitedLocation, x))
//							.map(x -> getDistance(new Location(x.getLongitude(), x.getLatitude()), visitedLocation.getLocation()))
//							.sorted()
//							.collect(Collectors.toList());
//		
//		List<TouristAttraction> fiveNearestTouristAttraction = allNearestTouristAttraction.stream()
//				.filter(x -> x.getDistance() < distancesToAttraction.get(5)).collect(Collectors.toList());
//
//		return fiveNearestTouristAttraction;
		
	}

	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				tracker.stopTracking();
			}
		});
	}

	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes
	// internal users are provided and stored in memory
//	private final Map<String, User> internalUserMap = new HashMap<>();

	public void initializeInternalUsers() {
		System.out.println("entree dans initializeInternalUsers");
		System.out.println(props.getInternalUserNumber());
		IntStream.range(0, props.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourguide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		attractions = gpsUtilProxy.getAttractions();
		System.out.println("------fin initialisation des users-------");
		logger.debug("Created " + props.getInternalUserNumber() + " internal test users.");
	}

	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
					new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

	/*
	 * Calcul les points de recommapense pour chaque endroit visit� par un utilisateur si ce dernier �tait proche d'une attraction
	 */
	public void calculateRewards(User user) {
		
		///////////////////// INITIAL METHOD /////////////////////
		for(VisitedLocation visitedLocation : user.getVisitedLocations()) {
			for(Attraction attraction : attractions) {
				if(user.getUserRewards().stream().filter(r -> r.getAttraction().getAttractionName().equals(attraction.getAttractionName())).count() == 0) {
					if(nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, rewardsProxy.getAttractionRewardPoints(attraction.getAttractionId(), user.getUserId())));
					}
				}
			}
		}
		
		///////////////////// NEW METHOD /////////////////////
//		List<UserReward> userRewards = attractions.stream()
//				.filter(x -> nearAttraction(user.getLastVisitedLocation(), x))
//				.map(x -> new UserReward(user.getLastVisitedLocation(), x,
//						rewardsProxy.getAttractionRewardPoints(x.getAttractionId(), user.getUserId())))
//				.collect(Collectors.toList());
//		user.addAllUserRewards(userRewards);
		
	}

	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(new Location(attraction.getLongitude(), attraction.getLatitude()), location) > props
				.getAttractionProximityRange() ? false : true;
	}

	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(new Location(attraction.getLongitude(), attraction.getLatitude()),
				visitedLocation.getLocation()) > props.getDefaultProximityBuffer() ? false : true;
	}

	public double getDistance(Location loc1, Location loc2) {
		double lat1 = Math.toRadians(loc1.getLatitude());
		double lon1 = Math.toRadians(loc1.getLongitude());
		double lat2 = Math.toRadians(loc2.getLatitude());
		double lon2 = Math.toRadians(loc2.getLongitude());

		double angle = Math
				.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		double statuteMiles = props.getSTATUTE_MILES_PER_NAUTICAL_MILE() * nauticalMiles;
		return statuteMiles;
	}

}
