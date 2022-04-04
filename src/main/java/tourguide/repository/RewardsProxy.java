package tourguide.repository;

import java.util.UUID;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import tourguide.configuration.CustomProperties;


@Component
public class RewardsProxy {
	
public int getAttractionRewardPoints(UUID attractionId, UUID userId, CustomProperties props) {
		String baseApiUrl = props.getApiUrlRewards(); 
		String apiUrl = baseApiUrl + "/getAttractionRewardPoints?attractionId=" + attractionId + "&userId=" + userId;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Integer> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, Integer.class);
		return response.getBody();
	}
	
}
