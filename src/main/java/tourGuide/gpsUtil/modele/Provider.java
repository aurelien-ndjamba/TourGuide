package tourGuide.gpsUtil.modele;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Provider {
	
	public String name;
	public double price;
	public UUID tripId;
	
}
