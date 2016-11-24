define(['./Author'], function (Author) {
    'use strict';
    function Game(jsonObj) {
    	if (jsonObj) {
    		this.name = jsonObj.name;
    		this.publishedYear = jsonObj.publishedYear;
    		this.playersMin = jsonObj.playersMin;
    		this.playersMax = jsonObj.playersMax;
    		this.playtimeMin = jsonObj.playtimeMin;
    		this.playtimeMax = jsonObj.playtimeMax;
    		this.playtimePerPlayer = jsonObj.playtimePerPlayer;
    		this.ageMin = jsonObj.ageMin;
    		this.description = jsonObj.description;
    		this.rating = jsonObj.rating;
    		this.authors = new Authors(jsonObj.authors);
    		// this.publishers = new Publishers(data.publishers);
    		// this.images = new Images(data.images);
    		// this.awards = new Awards(data.awards);
    		// this.notes = new Notes(data.notes);
        }
    };
    return Game;
});
