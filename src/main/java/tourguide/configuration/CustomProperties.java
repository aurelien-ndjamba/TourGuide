package tourguide.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
	public boolean isTestMode() {
		return testMode;
	}
	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}
	public int getInternalUserNumber() {
		return internalUserNumber;
	}
	public void setInternalUserNumber(int internalUserNumber) {
		this.internalUserNumber = internalUserNumber;
	}
	public long getTrackingPollingInterval() {
		return trackingPollingInterval;
	}
	public void setTrackingPollingInterval(long trackingPollingInterval) {
		this.trackingPollingInterval = trackingPollingInterval;
	}
	public String getApiUrlGpsUtil() {
		return apiUrlGpsUtil;
	}
	public void setApiUrlGpsUtil(String apiUrlGpsUtil) {
		this.apiUrlGpsUtil = apiUrlGpsUtil;
	}
	public String getApiUrlRewards() {
		return apiUrlRewards;
	}
	public void setApiUrlRewards(String apiUrlRewards) {
		this.apiUrlRewards = apiUrlRewards;
	}
	public String getApiUrlTripPricer() {
		return apiUrlTripPricer;
	}
	public void setApiUrlTripPricer(String apiUrlTripPricer) {
		this.apiUrlTripPricer = apiUrlTripPricer;
	}
	public double getSTATUTE_MILES_PER_NAUTICAL_MILE() {
		return STATUTE_MILES_PER_NAUTICAL_MILE;
	}
	public void setSTATUTE_MILES_PER_NAUTICAL_MILE(double sTATUTE_MILES_PER_NAUTICAL_MILE) {
		STATUTE_MILES_PER_NAUTICAL_MILE = sTATUTE_MILES_PER_NAUTICAL_MILE;
	}
	public int getDefaultProximityBuffer() {
		return defaultProximityBuffer;
	}
	public void setDefaultProximityBuffer(int defaultProximityBuffer) {
		this.defaultProximityBuffer = defaultProximityBuffer;
	}
	public int getAttractionProximityRange() {
		return attractionProximityRange;
	}
	public void setAttractionProximityRange(int attractionProximityRange) {
		this.attractionProximityRange = attractionProximityRange;
	}
	public int getThreadPool() {
		return threadPool;
	}
	public void setThreadPool(int threadPool) {
		this.threadPool = threadPool;
	}
}