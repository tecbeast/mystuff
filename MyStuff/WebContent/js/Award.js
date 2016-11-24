define(['./Country'], function (Country) {
    'use strict';
    function Award(data) {
		this.init(data);
    };
    Award.prototype.init = function (data) {
		this.id = (data && data.id) ? data.id : null;
        this.href = (data && data.link && data.link.self) ? data.link.self : null;
        this.name = (data && data.name) ? data.name : null;
        this.year = (data && data.year) ? data.year : 0;
        this.country = (data && data.country) ? new Country(data.country) : null;
    	return this;
    };
    return Award;
});
