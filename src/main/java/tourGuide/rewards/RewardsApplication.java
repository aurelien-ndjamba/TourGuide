package tourGuide.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import rewardCentral.RewardCentral;

@SpringBootApplication
public class RewardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RewardsApplication.class, args);
	}
	
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}

}
