package tourguide.modele;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TouristAttraction {
	
	private String attractionName;
	private Location touristLocation;
	private Location userLocation;
	private double distance;
	private int rewardPoints;
	public String getAttractionName() {
		return attractionName;
	}
	public Location getTouristLocation() {
		return touristLocation;
	}
	public Location getUserLocation() {
		return userLocation;
	}
	public double getDistance() {
		return distance;
	}
	public int getRewardPoints() {
		return rewardPoints;
	}
	
}
