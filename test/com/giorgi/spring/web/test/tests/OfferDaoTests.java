package com.giorgi.spring.web.test.tests;

import static org.junit.Assert.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.giorgi.spring.web.dao.Offer;
import com.giorgi.spring.web.dao.OffersDao;
import com.giorgi.spring.web.dao.User;
import com.giorgi.spring.web.dao.UsersDao;

@ActiveProfiles("dev")
@ContextConfiguration(locations = { "classpath:com/giorgi/spring/web/test/config/datasource.xml",
		"classpath:com/giorgi/spring/web/config/security-context.xml",
		"classpath:com/giorgi/spring/web/config/dao-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class OfferDaoTests {

	@Autowired
	private OffersDao offersDao;

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private DataSource dataSource;

	@Before
	public void init() {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);

		jdbc.execute("delete from offers");
		jdbc.execute("delete from users");
	}

	@Test
	public void testOffers() {

		User user = new User("giorgimode", "Giorgi Modebadze", "password",
				"giorgi@gmail.com", true, "user");

		assertTrue("User creation should return true", usersDao.create(user));

		Offer offer = new Offer(user, "This is a test offer.");

		assertTrue("Offer creation should return true", offersDao.create(offer));

		List<Offer> offers = offersDao.getOffers();

		assertEquals("Should be one offer in database.", 1, offers.size());

		assertEquals("Retrieved offer should match created offer.", offer,
				offers.get(0));

		// Get the offer with ID filled in.
		offer = offers.get(0);

		offer.setText("Updated offer text.");
		assertTrue("Offer update should return true", offersDao.update(offer));

		Offer updated = offersDao.getOffer(offer.getId());

		assertEquals("Updated offer should match retrieved updated offer",
				offer, updated);

		// Test get by ID ///////
		Offer offer2 = new Offer(user, "This is a test offer.");

		assertTrue("Offer creation should return true", offersDao.create(offer2));
		
		List<Offer> userOffers = offersDao.getOffers(user.getUsername());
		assertEquals("Should be two offers for user.", 2, userOffers.size());
		
		List<Offer> secondList = offersDao.getOffers();
		
		for(Offer current: secondList) {
			Offer retrieved = offersDao.getOffer(current.getId());
			
			assertEquals("Offer by ID should match offer from list.", current, retrieved);
		}
		
		// Test deletion
		offersDao.delete(offer.getId());

		List<Offer> finalList = offersDao.getOffers();

		assertEquals("Offers lists should contain one offer.", 1, finalList.size());
	}

}
