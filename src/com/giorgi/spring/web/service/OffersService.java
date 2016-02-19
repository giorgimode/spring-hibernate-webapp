package com.giorgi.spring.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.giorgi.spring.web.dao.Offer;
import com.giorgi.spring.web.dao.OffersDao;

@Service("offersService")
public class OffersService {

	private OffersDao offersDAO;

	@Autowired
	public void setOffersDAO(OffersDao offersDAO) {
		this.offersDAO = offersDAO;
	}


	public List<Offer> getCurrent(){
		return offersDAO.getOffers();
	}

	@Secured({"ROLE_USER","ROLE_ADMIN"})
	public void createOffer(Offer offer) {
		offersDAO.create(offer);
	}
}
