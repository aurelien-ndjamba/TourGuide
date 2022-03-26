package tourguide.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private CustomProperties props;

	public VisitedLocation getUserLocation(UUID userId) {
		String baseApiUrl = props.getApiUrlGpsUtil(); 
		String apiUrl = baseApiUrl + "/getUserLocation?userId=" + userId; //"http://localhost:9001"//baseApiUrl
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<VisitedLocation> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
				VisitedLocation.class);
		return response.getBody();
	}
	
//	public List<VisitedLocation> getUserLocation(List<UUID> userIds) {
////		String userIdsInString = new String();
////		userIds.forEach(x -> userIdsInString.concat("userId="+x+"&"));
//		
//		HttpEntity<List<UUID>> requestEntity = new HttpEntity<>(null, headers);
//		
//		
//		
//		String baseApiUrl = props.getApiUrlGpsUtil(); 
//		String apiUrl = baseApiUrl + "/getUserLocation?" + userIds+"="; //"http://localhost:9001"//baseApiUrl
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<List<VisitedLocation>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
//				new ParameterizedTypeReference<List<VisitedLocation>>() {
//		});
//		return response.getBody();
//	}

	public List<Attraction> getAttractions() {
		String baseApiUrl = props.getApiUrlGpsUtil(); 
		String apiUrl = baseApiUrl + "/getAttractions"; //"http://localhost:9001"
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<Attraction>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Attraction>>() {
				});
		return response.getBody();
	}

}
