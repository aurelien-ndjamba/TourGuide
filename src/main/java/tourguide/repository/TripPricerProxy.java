package tourguide.repository;

import java.util.List;
import java.util.UUID;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import tourguide.configuration.CustomProperties;
import tourguide.modele.Provider;

@Component
public class TripPricerProxy {

	public List<Provider> getPrice(String tripPricerApiKey, UUID userId, int numberOfAdults, int numberOfChildren,
			int tripDuration, int cumulatativeRewardPoints, CustomProperties props) {
		String baseApiUrl = props.getApiUrlTripPricer();
		String apiUrl = baseApiUrl + "/getPrice?tripPricerApiKey=" + tripPricerApiKey + "&userId=" + userId
				+ "&numberOfAdults=" + numberOfAdults + "&numberOfChildren=" + numberOfChildren + "&tripDuration="
				+ tripDuration + "&cumulatativeRewardPoints=" + cumulatativeRewardPoints;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<Provider>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Provider>>() {
				});
		return response.getBody();

	}

}
