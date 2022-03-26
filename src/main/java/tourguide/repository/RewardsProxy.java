package tourguide.repository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import tourguide.configuration.CustomProperties;


@Component
public class RewardsProxy {
	
	@Autowired
	private CustomProperties props;

	public int getAttractionRewardPoints(UUID attractionId, UUID userId) {
		String baseApiUrl = props.getApiUrlRewards();  //"http://localhost:9002"
		String apiUrl = baseApiUrl + "/getAttractionRewardPoints?attractionId=" + attractionId + "&userId=" + userId;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Integer> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, Integer.class);
		return response.getBody();
	}
	
}
