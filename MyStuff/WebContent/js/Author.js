define(['./Country'], function (Country) {
    'use strict';
    function Author(data) {
    	if (data) {
    		this.id = data.id;
            this.url = data.url;
            this.firstName = data.firstName;
            this.lastName = data.lastName;
            this.country = new Country(data.country);
        }
    };
    return Author;
});
