define(['./Config', './Author'], function (Config, Author) {
    'use strict';
    function Authors(data) {
    	this.clear();
        if (data && data.authors) {
        	for (var i = 0, a = data.authors; i < a.length; i++) {
                this.add(new Author(a[i]));
            }
        }
    };
    Authors.prototype.add = function (author) {
    	this.authors.push(author);
    };
    Authors.prototype.clear = function () {
    	this.authors = [];
    };
    Authors.prototype.findAll = function (callback) {
    	console.log('findAll');
        $.ajax({
        	type: 'GET',
        	url: Config.baseUrl + 'authors/',
        	dataType: "json",
        	success: function (data) {
        		callback(new Authors(data));
            }
        });
    };
    return Authors;
});
