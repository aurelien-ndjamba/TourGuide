package tourGuide.gpsUtil.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.gpsUtil.service.GpsUtilService;


@RestController
public class GpsUtilController {
	
	@Autowired
	GpsUtilService gpsUtilService;
	
    @RequestMapping("/getUserLocation") 
    public VisitedLocation getUserLocation(@RequestParam UUID userId) {
    	return gpsUtilService.getUserLocation(userId);
    }
    
    @RequestMapping("/getAttractions") 
    public List<Attraction> getAttractions() {
    	return gpsUtilService.getAttractions();
    }

}
