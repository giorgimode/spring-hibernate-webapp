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

import com.giorgi.spring.web.dao.User;
import com.giorgi.spring.web.dao.UsersDao;

@ActiveProfiles("dev")
@ContextConfiguration(locations = { "classpath:com/giorgi/spring/web/test/config/datasource.xml",
		"classpath:com/giorgi/spring/web/config/security-context.xml",
		"classpath:com/giorgi/spring/web/config/dao-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class UserDaoTests {

	@Autowired
	private UsersDao usersDao;
	@Autowired
	private DataSource dataSource;

	@Before
	public void init() {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		jdbc.execute("delete from users");
	}

	@Test
	public void testCreateUser() {
		User user = new User("georgemode","Giorgi Modebadze", "mypassword", "george@gg.com", true, "ROLE_USER");
		assertTrue("User creation should return true", usersDao.create(user));
		
		List<User> users = usersDao.getAllUsers();
		assertEquals("Number of users should be n", 1, users.size());
		assertTrue(usersDao.exists(user.getUsername()));
		assertEquals(user, users.get(0));
	}
	
	
}
