package tourguide.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import tourguide.modele.Location;
import tourguide.modele.TouristAttraction;
import tourguide.modele.User;
import tourguide.service.TourguideService;

@RestController
public class TourGuideController {

	@Autowired
	private TourguideService TourguideService;

	@RequestMapping("/")
	public String index() { 
		return "Greetings from Tourguide!";
	}

	@RequestMapping("/getLocation")
	public String getLocation(@RequestParam String userName) {
		return JsonStream.serialize(TourguideService.getUserLocation(userName).getLocation());
	}

	// TODO: Change this method to no longer return a List of Attractions.
	// Instead: Get the closest five tourist attractions to the user - no matter how
	// far away they are.
	// Return a new JSON object that contains:
	// Name of Tourist attraction,
	// Tourist attractions lat/long,
	// The user's location lat/long,
	// The distance in miles between the user's location and each of the
	// attractions.
	// The reward points for visiting each Attraction.
	// Note: Attraction reward points can be gathered from RewardsCentral
	@RequestMapping("/getNearbyAttractions")
	public  List<TouristAttraction> getNearbyAttractions(@RequestParam String userName) {
		return TourguideService.getNearByAttractions(userName); 
	}

	@RequestMapping("/getRewards")
	public String getRewards(@RequestParam String userName) {
		return JsonStream.serialize(TourguideService.getUserRewards(getUser(userName)));
	}

	@RequestMapping("/getAllCurrentLocations")
	public List<Map<UUID, Location>> getAllCurrentLocations() {
		// TODO: Get a list of every user's most recent location as JSON
		// - Note: does not use gpsUtil to query for their current location,
		// but rather gathers the user's current location from their stored location
		// history.
		//
		// Return object should be the just a JSON mapping of userId to Locations
		// similar to:
		// {
		// "019b04a9-067a-4c76-8817-ee75088c3822":
		// {"longitude":-48.188821,"latitude":74.84371}
		// ...
		// }
		return TourguideService.getAllCurrentLocations();
	}

	@RequestMapping("/getTripDeals")
	public String getTripDeals(@RequestParam String userName, @RequestParam int numberOfAdults, @RequestParam int numberOfChildren,
			@RequestParam int tripDuration) {
		return JsonStream.serialize(TourguideService.getTripDeals(userName, numberOfAdults, numberOfChildren, tripDuration));
	}

	@RequestMapping("/getUser")
	private User getUser(@RequestParam String userName) {
		return TourguideService.getUser(userName);
	}

}