package com.balancedbytes.mystuff.games;

import javax.xml.bind.annotation.XmlRootElement;

import com.balancedbytes.mystuff.RestData;

@XmlRootElement
public class Game extends RestData {

	private String title;
	private String subtitle;
	private Integer publishedYear;
	private Integer playersMin;
	private Integer playersMax;
	private Integer playtimeMin;
	private Integer playtimeMax;
	private Boolean playtimePerPlayer;
	private Integer ageMin;
	private String description;
	private Integer rating;
	private Authors authors;
	private Publishers publishers;
	private Images images;
	private Awards awards;
	private Notes notes;

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String name) {
		this.title = name;
	}
	
	public String getSubtitle() {
		return subtitle;
	}
	
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	
	public Integer getPublishedYear() {
		return publishedYear;
	}
	
	public void setPublishedYear(Integer editionYear) {
		this.publishedYear = editionYear;
	}
	
	public Integer getPlayersMin() {
		return playersMin;
	}
	
	public void setPlayersMin(Integer playersMin) {
		this.playersMin = playersMin;
	}
	
	public Integer getPlayersMax() {
		return playersMax;
	}
	
	public void setPlayersMax(Integer playersMax) {
		this.playersMax = playersMax;
	}
	
	public Integer getPlaytimeMin() {
		return playtimeMin;
	}
	
	public void setPlaytimeMin(Integer playtimeMin) {
		this.playtimeMin = playtimeMin;
	}
	
	public Integer getPlaytimeMax() {
		return playtimeMax;
	}
	
	public void setPlaytimeMax(Integer playtimeMax) {
		this.playtimeMax = playtimeMax;
	}
	
	public Boolean getPlaytimePerPlayer() {
		return playtimePerPlayer;
	}
	
	public void setPlaytimePerPlayer(Boolean playtimePerPlayer) {
		this.playtimePerPlayer = playtimePerPlayer;
	}
	
	public Integer getAgeMin() {
		return ageMin;
	}
	
	public void setAgeMin(Integer ageMin) {
		this.ageMin = ageMin;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getRating() {
		return rating;
	}
	
	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Authors getAuthors() {
		return authors;
	}
	
	public void setAuthors(Authors authors) {
		this.authors = authors;
	}
	
	public Publishers getPublishers() {
		return publishers;
	}
	
	public void setPublishers(Publishers publishers) {
		this.publishers = publishers;
	}
	
	public Images getImages() {
		return images;
	}
	
	public void setImages(Images images) {
		this.images = images;
	}
	
	public Awards getAwards() {
		return awards;
	}
	
	public void setAwards(Awards awards) {
		this.awards = awards;
	}
	
	public Notes getNotes() {
		return notes;
	}
	
	public void setNotes(Notes notes) {
		this.notes = notes;
	}

}
