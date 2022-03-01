package tourGuide.gpsUtil.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

@Service
public class GpsUtilService {
	
	@Autowired
	private GpsUtil gpsUtil;
	
	public VisitedLocation getUserLocation(UUID userId) {
		return gpsUtil.getUserLocation(userId);
	}

	public List<Attraction> getAttractions() {
		return gpsUtil.getAttractions();
	}

}
