package tourguide.modele;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attraction {
	 
	private String attractionName;
	private String city;
	private String state;
	private UUID attractionId;
	private double longitude;
	private double latitude;
	
}
