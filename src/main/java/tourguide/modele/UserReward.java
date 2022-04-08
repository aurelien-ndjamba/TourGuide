package tourguide.modele;

public class UserReward {

	private final VisitedLocation visitedLocation;
	private final Attraction attraction;
	private int rewardPoints;

	public UserReward(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
		this.rewardPoints = rewardPoints;
	}

	public Attraction getAttraction() {
		return attraction;
	}

	public int getRewardPoints() {
		return rewardPoints;
	}

}
