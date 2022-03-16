package tourGuide.tripPricer.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tourGuide.tripPricer.service.tripPricerService;
import tripPricer.Provider;

@RestController
public class tripPricerController {
	
	@Autowired
	private tripPricerService tripPricerService;
	 
	@RequestMapping("/getPrice") 
	public List<Provider> getPrice(@RequestParam String tripPricerApiKey, @RequestParam UUID userId, @RequestParam int numberOfAdults, @RequestParam int numberOfChildren, @RequestParam int tripDuration, @RequestParam int cumulatativeRewardPoints) {
		return tripPricerService.getPrice(tripPricerApiKey, userId, numberOfAdults, numberOfChildren, tripDuration, cumulatativeRewardPoints);
	}

}
