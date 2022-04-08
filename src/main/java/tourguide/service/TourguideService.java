package tourguide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

//@Data
@Service
public class TourguideService {

	public Tracker getTracker() {
		return tracker;
	}

	private Logger logger = LoggerFactory.getLogger(TourguideService.class);

	@Autowired
	private final CustomProperties props;
	@Autowired
	private final GpsUtilProxy gpsUtilProxy;
	@Autowired
	private final RewardsProxy rewardsProxy;
	@Autowired
	private final TripPricerProxy tripPricerProxy;
	public ExecutorService executorService;
	public final Map<String, User> internalUserMap = new HashMap<>();
	public static List<Attraction> attractions = new ArrayList<Attraction>();
	public final Tracker tracker;

	/**
	 * Constructeur TourguideService
	 * 
	 * @param GpsUtilProxy
	 * @param RewardsProxy
	 * @param TripPricerProxy
	 * @param CustomProperties
	 * 
	 */
	public TourguideService(GpsUtilProxy gpsUtilProxy, RewardsProxy rewardsProxy, TripPricerProxy tripPricerProxy,
			CustomProperties props) {
		this.gpsUtilProxy = gpsUtilProxy;
		this.rewardsProxy = rewardsProxy;
		this.tripPricerProxy = tripPricerProxy;
		this.props = props;
		;
		if (props.isTestMode()) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}

	/**
	 * Méthode pour initialiser des utilisateurs pour des tests internes
	 * 
	 * @return void
	 */
	public void initializeInternalUsers() {
		IntStream.range(0, props.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourguide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);
			internalUserMap.put(userName, user);
		});
		attractions = gpsUtilProxy.getAttractions(props);
		logger.debug("Created " + props.getInternalUserNumber() + " internal test users.");
	}

	/**
	 * Méthode pour générer un historique aléatoire de localisation d'un utilisateur
	 * 
	 * @param User
	 * @return void
	 */
	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
					new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	/**
	 * Méthode pour calculer les récompenses d'un utilisateur
	 * 
	 * @param User
	 * @return void
	 */
	public void calculateRewards(User user) {
		List<VisitedLocation> visitedLocations = user.getVisitedLocations();
		List<UserReward> userRewards = user.getUserRewards();

		for (VisitedLocation visitedLocation : visitedLocations) {
			for (Attraction attraction : attractions) {
				long matching = userRewards.stream()
						.filter(r -> r.getAttraction().getAttractionName().equals(attraction.getAttractionName()))
						.count();
				if (matching == 0) {
					if (nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, rewardsProxy
								.getAttractionRewardPoints(attraction.getAttractionId(), user.getUserId(), props)));
					}
				}
			}
		}

	}

	/**
	 * Méthode pour calculer les récompenses de plusieurs utilisateurs
	 * 
	 * @param List<User>
	 * @return void
	 */
	public void calculateRewards(List<User> users) throws InterruptedException {
		executorService = Executors.newFixedThreadPool(props.getThreadPool());
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			Runnable task = () -> {
				calculateRewards(user);
			};
			executorService.submit(task);
		}
		executorService.shutdown();
	}

	/**
	 * Méthode pour obtenir la liste de tous les utilisateurs
	 * 
	 * @return List<User>
	 */
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	/**
	 * Méthode pour obtenir la liste de récompense d'un utilisateur
	 * 
	 * @param User
	 * @return List<UserReward>
	 */
	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	/**
	 * Méthode pour obtenir la liste de récompense d'un utilisateur
	 * 
	 * @param String
	 * @return List<UserReward>
	 */
	public List<UserReward> getUserRewards(String userName) {
		User user = internalUserMap.get(userName);
		return user.getUserRewards();
	}

	/**
	 * Méthode pour obtenir l'emplacement d'un utilisateur
	 * 
	 * @param String
	 * @return Location
	 * 
	 */
	public Location getLocation(String userName) {
		return getUserLocation(userName).getLocation();
	}

	/**
	 * Méthode pour obtenir l'emplacement d'un utilisateur
	 * 
	 * @param String
	 * @return VisitedLocation
	 */
	public VisitedLocation getUserLocation(String userName) {
		User user = internalUserMap.get(userName);
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
				: trackUserLocation(user);
		return visitedLocation;
	}

	/**
	 * Méthode pour ajouter un utilisateur
	 * 
	 * @param User
	 * @return void
	 */
	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	/**
	 * Méthode pour obtenir des offres de voyages d'un utilisateur en fonction de
	 * ses critères de recherche
	 * 
	 * @param String
	 * @param int
	 * @param int
	 * @param int
	 * @return List<Provider>
	 * 
	 */
	public List<Provider> getTripDeals(String userName, int numberOfAdults, int numberOfChildren, int tripDuration) {
		User user = internalUserMap.get(userName);
		user.getUserPreferences().setNumberOfAdults(numberOfAdults);
		user.getUserPreferences().setNumberOfChildren(numberOfChildren);
		user.getUserPreferences().setTripDuration(tripDuration);
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricerProxy.getPrice(tripPricerApiKey, user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints, props);
		user.setTripDeals(providers);
		return providers;
	}

	/**
	 * Méthode pour obtenir l'emplacement visité d'un utilisateur
	 * 
	 * @param User
	 * @return VisitedLocation
	 * 
	 */
	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtilProxy.getUserLocation(user.getUserId(), props);
		user.addToVisitedLocations(visitedLocation);
		calculateRewards(user);
		return visitedLocation;
	}

	/**
	 * Méthode pour obtenir les emplacements visités de plusieurs utilisateurs
	 * 
	 * @param List<User>
	 * @return void
	 * 
	 */
	public void trackUserLocation(List<User> users) {
		List<Callable<VisitedLocation>> tasks = new ArrayList<>();
		executorService = Executors.newFixedThreadPool(props.getThreadPool());
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			Callable<VisitedLocation> task = () -> {
				return trackUserLocation(user);
			};
			tasks.add(task);
		}
		try {
			executorService.invokeAll(tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executorService.shutdown();

	}

	/**
	 * Méthode pour obtenir la position courante de tous les utilisateurs
	 * simultanément
	 * 
	 * @return List<Map<UUID, Location>>
	 * 
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

	/**
	 * Méthode pour obtenir la liste des cinq attractions touristiques les plus
	 * proches d'utilisateur
	 * 
	 * @param String
	 * @return List<TouristAttraction>
	 * 
	 */
	public List<TouristAttraction> getNearByAttractions(String userName) {
		VisitedLocation visitedLocation = internalUserMap.get(userName).getLastVisitedLocation();
		props.setDefaultProximityBuffer(Integer.MAX_VALUE);

		List<TouristAttraction> allNearestTouristAttraction = attractions.stream()
				.filter(x -> nearAttraction(visitedLocation, x))
				.map(x -> new TouristAttraction(x.getAttractionName(), new Location(x.getLongitude(), x.getLatitude()),
						visitedLocation.getLocation(),
						getDistance(new Location(x.getLongitude(), x.getLatitude()), visitedLocation.getLocation()),
						rewardsProxy.getAttractionRewardPoints(x.getAttractionId(), visitedLocation.getUserId(),
								props)))
				.collect(Collectors.toList());

		List<Double> distancesToAttraction = attractions.stream().filter(x -> nearAttraction(visitedLocation, x))
				.map(x -> getDistance(new Location(x.getLongitude(), x.getLatitude()), visitedLocation.getLocation()))
				.sorted().collect(Collectors.toList());

		List<TouristAttraction> fiveNearestTouristAttraction = allNearestTouristAttraction.stream()
				.filter(x -> x.getDistance() < distancesToAttraction.get(5)).collect(Collectors.toList());

		return fiveNearestTouristAttraction;
	}

	/**
	 * Méthode pour arrêter un thread en cours d'exécution
	 * 
	 * @return void
	 * 
	 */
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

	/**
	 * Méthode pour générer une longitude aléatoirement
	 * 
	 * @return double
	 * 
	 */
	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 * Méthode pour générer une latitude aléatoirement
	 * 
	 * @return double
	 * 
	 */
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 * Méthode pour générer une date aléatoirement
	 * 
	 * @return Date
	 * 
	 */
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

	/**
	 * Méthode pour vérifier si un emplacement est proche d'une attraction
	 * 
	 * @param Attraction
	 * @param Location
	 * @return boolean
	 * 
	 */
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(new Location(attraction.getLongitude(), attraction.getLatitude()), location) > props
				.getAttractionProximityRange() ? false : true;
	}

	/**
	 * Méthode pour vérifier si un emplacement visité est proche d'une attraction
	 * 
	 * @param Attraction
	 * @param VisitedLocation
	 * @return boolean
	 * 
	 */
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(new Location(attraction.getLongitude(), attraction.getLatitude()),
				visitedLocation.getLocation()) > props.getDefaultProximityBuffer() ? false : true;
	}

	/**
	 * Méthode pour obtenir la distance entre deux emplacements
	 * 
	 * @param Location
	 * @param Location
	 * @return double
	 * 
	 */
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

	/**
	 * Méthode pour obtenir la liste de toutes les attractions
	 * 
	 * @return List<Attraction>
	 * 
	 */
	public List<Attraction> getAttractions() {
		return attractions;
	}

//	/**
//	 * Méthode pour fixer une liste d'attractions
//	 * 
//	 * @param List<Attraction>
//	 * @return void
//	 * 
//	 */
//	public static void setAttractions(List<Attraction> attractions) {
//		TourguideService.attractions = attractions;
//	}

}
