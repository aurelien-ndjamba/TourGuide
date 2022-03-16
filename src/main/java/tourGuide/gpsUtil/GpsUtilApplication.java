package tourGuide.gpsUtil;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import gpsUtil.GpsUtil;

@SpringBootApplication
public class GpsUtilApplication {

	public static void main(String[] args) {
		Locale.setDefault(Locale.US); 
		SpringApplication.run(GpsUtilApplication.class, args);
	}
	
	@Bean
	public GpsUtil getGpsUtil() {
		return new GpsUtil();
	}

}
