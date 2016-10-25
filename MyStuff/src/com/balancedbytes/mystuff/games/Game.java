package com.balancedbytes.mystuff.games;

import java.sql.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Game {

	@XmlAttribute
	private long id;  // primary key
	private String name;
	private int editionYear;
	private int playersMin;
	private int playersMax;
	private int playtimeMin;
	private int playtimeMax;
	private boolean playtimePerPlayer;
	private int ageMin;
	private Date lastPlayed;
	private int rating;
	@XmlElementWrapper(name="authors")
	@XmlElement(name="author")
	private List<Author> authors;
	@XmlElementWrapper(name="publishers")
	@XmlElement(name="publisher")
	private List<Publisher> publishers;

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getEditionYear() {
		return editionYear;
	}
	
	public void setEditionYear(int editionYear) {
		this.editionYear = editionYear;
	}
	
	public int getPlayersMin() {
		return playersMin;
	}
	
	public void setPlayersMin(int playersMin) {
		this.playersMin = playersMin;
	}
	
	public int getPlayersMax() {
		return playersMax;
	}
	
	public void setPlayersMax(int playersMax) {
		this.playersMax = playersMax;
	}
	
	public int getPlaytimeMin() {
		return playtimeMin;
	}
	
	public void setPlaytimeMin(int playtimeMin) {
		this.playtimeMin = playtimeMin;
	}
	
	public int getPlaytimeMax() {
		return playtimeMax;
	}
	
	public void setPlaytimeMax(int playtimeMax) {
		this.playtimeMax = playtimeMax;
	}
	
	public boolean isPlaytimePerPlayer() {
		return playtimePerPlayer;
	}
	
	public void setPlaytimePerPlayer(boolean playtimePerPlayer) {
		this.playtimePerPlayer = playtimePerPlayer;
	}
	
	public int getAgeMin() {
		return ageMin;
	}
	
	public void setAgeMin(int ageMin) {
		this.ageMin = ageMin;
	}
	
	public Date getLastPlayed() {
		return lastPlayed;
	}
	
	public void setLastPlayed(Date lastPlayed) {
		this.lastPlayed = lastPlayed;
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<Publisher> getPublishers() {
		return publishers;
	}
	
	public void setPublishers(List<Publisher> publishers) {
		this.publishers = publishers;
	}

}
