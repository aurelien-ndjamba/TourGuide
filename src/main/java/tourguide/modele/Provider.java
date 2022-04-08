package tourguide.modele;

import java.util.UUID;

public class Provider {
	
	private String name;
	private double price;
	private UUID tripId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public UUID getTripId() {
		return tripId;
	}
	public void setTripId(UUID tripId) {
		this.tripId = tripId;
	}
	
}
