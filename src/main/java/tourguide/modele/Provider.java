package tourguide.modele;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Provider {
	
	private String name;
	private double price;
	private UUID tripId;
	
}
