package com.giorgi.spring.web.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;

@Component("offersDAO")
public class OffersDao {

	private NamedParameterJdbcTemplate jdbc;

	@Autowired
	public void setDataSource(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}

	public List<Offer> getOffers() {

		return jdbc.query("select * from offers", new RowMapper<Offer>() {

			@Override
			public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
				Offer offer = new Offer();
				offer.setId(rs.getInt("id"));
				offer.setName(rs.getString("name"));
				offer.setEmail(rs.getString("email"));
				offer.setText(rs.getString("text"));

				return offer;
			}
		});
	}

	/*
	 * public List<Offer> getOffers() { MapSqlParameterSource params = new
	 * MapSqlParameterSource(); params.addValue("name", "Sue");
	 * 
	 * return jdbc.query("select * from offers where name=:name", params, new
	 * RowMapper<Offer>() {
	 * 
	 * public Offer mapRow(ResultSet rs, int rowNum) throws SQLException { Offer
	 * offer = new Offer(); offer.setId(rs.getInt("id"));
	 * offer.setName(rs.getString("name"));
	 * offer.setEmail(rs.getString("email"));
	 * offer.setText(rs.getString("text"));
	 * 
	 * return offer; } }); }
	 */

	public int[] create(List<Offer> offers) {
		SqlParameterSource[] paramList = SqlParameterSourceUtils
				.createBatch(offers.toArray());

		// old jdbc way of doing it
		// jdbc.getJdbcOperations().batchUpdate(sql)

		return jdbc.batchUpdate(
				"insert into offers (name, text, email) values (:name, :text, :email)",
				paramList);

	}

	public boolean create(Offer offer) {
		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(
				offer);

		return jdbc.update(
				"insert into offers (name, text, email) values (:name, :text, :email)",
				params) == 1;

	}

	public boolean delete(int id) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		return jdbc.update("delete from offers where id=:id", params) == 1;

	}

	public Offer getOffer(int id) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		return jdbc.queryForObject("select * from offers where id=:id", params,
				new RowMapper<Offer>() {

					@Override
					public Offer mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Offer offer = new Offer();
						offer.setId(rs.getInt("id"));
						offer.setName(rs.getString("name"));
						offer.setEmail(rs.getString("email"));
						offer.setText(rs.getString("text"));

						return offer;
					}
				});
	}

}
