package com.giorgi.spring.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.giorgi.spring.web.dao.User;
import com.giorgi.spring.web.service.UsersService;

@Controller
public class LoginController {

	private UsersService usersService;

	@Autowired
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	@RequestMapping("/admin")
	public String showAdmin(Model model) {
		List<User> allUsers = usersService.getAllUsers();

		model.addAttribute("users", allUsers);

		return "admin";
	}

	@RequestMapping("/login")
	public String showLogin() {

		return "login";
	}

	@RequestMapping("/loggedout")
	public String showLoggedout() {

		return "loggedout";
	}

	@RequestMapping("/denied")
	public String showDenied() {

		return "denied";
	}

	@RequestMapping("/newaccount")
	public String showNewAccount(Model model) {
		model.addAttribute("user", new User());
		return "newaccount";
	}

	@RequestMapping(value = "/createaccount", method = RequestMethod.POST)
	public String createAccount(@Valid User user, BindingResult result) {

		if (result.hasErrors()) {
			return "newaccount";
		}

		user.setAuthority("ROLE_USER");
		user.setEnabled(true);

		if (usersService.exists(user.getUsername())) {
			result.rejectValue("username", "DuplicateKey.user.username");

			return "newaccount";
		}

		usersService.create(user);

		return "accountcreated";
	}
}