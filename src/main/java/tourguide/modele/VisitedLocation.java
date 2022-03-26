package tourguide.modele;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitedLocation {
	
	private UUID userId;
	private Location location;
	private Date timeVisited; 

}
