define(function () {
    'use strict';
    function Country(data) {
    	if (data) {
    		this.code = data.code;
    		this.name = data.name;
    	}
    }
    return Country;
});
