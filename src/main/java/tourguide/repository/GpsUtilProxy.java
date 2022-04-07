package tourguide.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import tourguide.configuration.CustomProperties;
import tourguide.modele.Attraction;
import tourguide.modele.VisitedLocation;

@Component
public class GpsUtilProxy {

	public VisitedLocation getUserLocation(UUID userId, CustomProperties props) {
		String baseApiUrl = props.getApiUrlGpsUtil();
		String apiUrl = baseApiUrl + "/getUserLocation?userId=" + userId;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<VisitedLocation> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
				VisitedLocation.class);
		return response.getBody();
	}

	public List<Attraction> getAttractions(CustomProperties props) {
		String baseApiUrl = props.getApiUrlGpsUtil();
		String apiUrl = baseApiUrl + "/getAttractions";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<Attraction>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Attraction>>() {
				});
		return response.getBody();
	}

}
