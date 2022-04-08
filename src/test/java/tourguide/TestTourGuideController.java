package tourguide;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import tourguide.controller.TourGuideController;
import tourguide.service.TourguideService;

@WebMvcTest(controllers = TourGuideController.class)
public class TestTourGuideController {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
    private TourguideService tourguideService;

	@Test
	public void testIndex() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk());
	}
	
	@Test
	public void testGetLocation() throws Exception {
		mockMvc.perform(get("/getLocation?userName=internalUser0")).andExpect(status().isOk());
	}
	
	@Test
	public void testGetNearbyAttractions() throws Exception {
		mockMvc.perform(get("/getNearbyAttractions?userName=internalUser0")).andExpect(status().isOk());
	}
	
	@Test
	public void testGetRewards() throws Exception {
		mockMvc.perform(get("/getRewards?userName=internalUser0")).andExpect(status().isOk());
	}
	
	@Test
	public void testGetAllCurrentLocations() throws Exception {
		mockMvc.perform(get("/getAllCurrentLocations")).andExpect(status().isOk());
	}
	
	@Test
	public void testGetTripDeals() throws Exception {
		mockMvc.perform(get("/getTripDeals?userName=internalUser0&numberOfAdults=9&numberOfChildren=0&tripDuration=1&cumulatativeRewardPoints=0")).andExpect(status().isOk());
	}

}
