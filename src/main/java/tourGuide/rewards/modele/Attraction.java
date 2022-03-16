package tourGuide.rewards.modele;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attraction {
	 
	public String attractionName;
	public String city;
	public String state;
	public UUID attractionId;
	public double longitude;
	public double latitude;
	

}
