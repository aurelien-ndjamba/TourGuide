package tourguide.modele;

import java.util.UUID;

public class Attraction {
	 
	private String attractionName;
	private String city;
	private String state;
	private UUID attractionId;
	private double longitude;
	private double latitude;
	public String getAttractionName() {
		return attractionName;
	}
	public void setAttractionName(String attractionName) {
		this.attractionName = attractionName;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setState(String state) {
		this.state = state;
	}
	public UUID getAttractionId() {
		return attractionId;
	}
	public void setAttractionId(UUID attractionId) {
		this.attractionId = attractionId;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
}
