package tourGuide.tripPricer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import tripPricer.TripPricer;

@SpringBootApplication
public class TripPricerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripPricerApplication.class, args);
	}
	
	@Bean
	public TripPricer getTripPricer() {
		return new TripPricer();
	}

}
