package tourGuide.rewards.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tourGuide.rewards.service.RewardsService;

@RestController
public class RewardsController {

	@Autowired
	RewardsService rewardsService;

	@RequestMapping("/getAttractionRewardPoints")
	public int getAttractionRewardPoints(@RequestParam UUID attractionId, @RequestParam UUID userId) {
		return rewardsService.getAttractionRewardPoints(attractionId, userId);
	}

}
