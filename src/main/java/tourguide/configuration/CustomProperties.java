package tourguide.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix="com.tourguide")
public class CustomProperties {
	
	private boolean testMode;
	private int internalUserNumber;
	private long trackingPollingInterval;
	private String apiUrlGpsUtil;
	private String apiUrlRewards;
	private String apiUrlTripPricer;
	private double STATUTE_MILES_PER_NAUTICAL_MILE;
	private int defaultProximityBuffer;
	private int attractionProximityRange;
	private int threadPool;
	
}
