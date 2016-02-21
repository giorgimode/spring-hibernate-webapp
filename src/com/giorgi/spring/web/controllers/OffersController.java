package com.giorgi.spring.web.controllers;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.giorgi.spring.web.dao.Offer;
import com.giorgi.spring.web.service.OffersService;

@Controller
public class OffersController {
	private OffersService offersService;

	@Autowired
	public void setOffersService(OffersService offersService) {
		this.offersService = offersService;
	}

	/*
	 * @RequestMapping("/") public String showHome(HttpSession session){
	 * session.setAttribute("name", "Boris"); return "home"; }
	 */

	/*
	 * @RequestMapping("/") public ModelAndView showHome(){ ModelAndView mv =
	 * new ModelAndView("home");
	 * 
	 * Map<String, Object> model = mv.getModel();
	 * 
	 * model.put("name", "Giorgi Modebadze");
	 * 
	 * return mv; }
	 */

	@RequestMapping("/offers")
	public String showOffers(Model model) {

		List<Offer> offers = offersService.getCurrent();

		model.addAttribute("offers", offers);

		return "offers";
	}

	
	@RequestMapping("/createoffer")
	public String createOffer(Model model) {

		model.addAttribute("offer", new Offer());

		return "createoffer";
	}

	@RequestMapping(value = "/docreate", method = RequestMethod.POST)
	public String docreate(Model model, @Valid Offer offer,
			BindingResult result, Principal principal) {

		if (result.hasErrors()) {

			return "createoffer";
		}
		
		offer.getUser().setUsername(principal.getName());;
		offersService.createOffer(offer);
		
		return "offercreated";
	}
	
}
