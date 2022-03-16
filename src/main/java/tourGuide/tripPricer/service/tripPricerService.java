package tourGuide.tripPricer.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class tripPricerService {
	
	@Autowired 
	private TripPricer tripPricer; 

	public List<Provider> getPrice(String tripPricerApiKey, UUID userId, int numberOfAdults, int numberOfChildren, int tripDuration, int cumulatativeRewardPoints) {
		return tripPricer.getPrice(tripPricerApiKey, userId, numberOfAdults, numberOfChildren, tripDuration, cumulatativeRewardPoints);
	}

}
