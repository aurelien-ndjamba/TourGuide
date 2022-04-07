package tourguide.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tourguide.modele.Location;
import tourguide.modele.Provider;
import tourguide.modele.TouristAttraction;
import tourguide.modele.UserReward;
import tourguide.service.TourguideService;

/**
 * Controller TourGuideController
 */
@RestController
public class TourGuideController {
	
	private Logger logger = LoggerFactory.getLogger(TourGuideController.class);

	@Autowired
	private TourguideService tourguideService;

	/**
	 * Méthode pour afficher la page d'accueil de l'application
	 * 
	 * @return String
	 * 
	 */
	@RequestMapping("/")
	public String index() {
		logger.debug("Accéder à la page d'accueil de l'application");
		return "Greetings from Tourguide!";
	}
 
	/**
	 * Méthode pour afficher l'emplacement d'un utilisateur
	 * 
	 * @param String
	 * @return Location
	 * 
	 */
	@RequestMapping("/getLocation")
	public Location getLocation(@RequestParam String userName) {
		logger.debug("Obtenir l'emplacement d'un utilisateur");
		return tourguideService.getUserLocation(userName).getLocation();
	}

	/**
	 * Méthode pour afficher la liste de cinq attractions les plus proches d'un utlisateur
	 * 
	 * @param String
	 * @return List<TouristAttraction>
	 * 
	 */
	@RequestMapping("/getNearbyAttractions")
	public List<TouristAttraction> getNearbyAttractions(@RequestParam String userName) {
		logger.debug("Obtenir la liste de cinq attractions les plus proches d'un utlisateur");
		return tourguideService.getNearByAttractions(userName);
	}

	/**
	 * Méthode pour afficher la liste des points de récompense d'un utlisateur
	 * 
	 * @param String
	 * @return List<UserReward>
	 * 
	 */
	@RequestMapping("/getRewards")
	public List<UserReward> getUserRewards(@RequestParam String userName) {
		logger.debug("Obtenir la liste des points de récompense d'un utlisateur");
		return tourguideService.getUserRewards(userName);
	}

	/**
	 * Méthode pour afficher l'emplacement courant de tous les utilisateurs simultanément
	 * 
	 * @return List<Map<UUID, Location>>
	 * 
	 */
	@RequestMapping("/getAllCurrentLocations")
	public List<Map<UUID, Location>> getAllCurrentLocations() {
		logger.debug("Obtenir l'emplacement courant de tous les utilisateurs simultanément.");
		return tourguideService.getAllCurrentLocations();
	}

	/**
	 * Méthode pour afficher les offres de voyages d'un utlisateur en fonction des critères de recherches
	 * 
	 * @param String
	 * @param int
	 * @param int
	 * @param int
	 * @return List<UserReward>
	 * 
	 */
	@RequestMapping("/getTripDeals")
	public List<Provider> getTripDeals(@RequestParam String userName, @RequestParam int numberOfAdults,
			@RequestParam int numberOfChildren, @RequestParam int tripDuration) {
		logger.debug("Obtenir les offres de voyages d'un utlisateur en fonction de ses critères de recherches");
		return tourguideService.getTripDeals(userName, numberOfAdults, numberOfChildren, tripDuration);
	}

}

