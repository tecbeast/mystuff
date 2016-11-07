package com.balancedbytes.mystuff.games;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.balancedbytes.mystuff.RestData;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Game extends RestData {

	private String name;
	private int editionYear;
	private int playersMin;
	private int playersMax;
	private int playtimeMin;
	private int playtimeMax;
	private boolean playtimePerPlayer;
	private int ageMin;
	private String description;
	private int rating;
	private Authors authors;
	private Publishers publishers;
	private Images images;
	private Awards awards;
	private Notes notes;

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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
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
