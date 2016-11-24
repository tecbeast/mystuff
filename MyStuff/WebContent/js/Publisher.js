define(['./Country'], function (Country) {
    'use strict';
    function Publisher(data) {
    	this.init(data);
    };
    Publisher.prototype.init = function (data) {
		this.id = (data && data.id) ? data.id : null;
        this.href = (data && data.link && data.link.self) ? data.link.self : null;
        this.name = (data && data.name) ? data.name : null;
        this.country = (data && data.country) ? new Country(data.country) : null;
    	return this;
    };
    return Publisher;
});
