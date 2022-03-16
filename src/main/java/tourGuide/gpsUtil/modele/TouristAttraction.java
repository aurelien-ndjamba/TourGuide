package tourGuide.gpsUtil.modele;

import gpsUtil.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TouristAttraction {
	
	private String attractionName;
	private Location touristLocation;
	private Location userLocation;
	private double distance;
	private double rewardPoints;
	
}
