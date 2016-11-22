define(['./Authors'], function (Authors) {
    'use strict';
    function start() {
        new Authors().findAll(callback);
    }
    function callback(authors) {
        for (var _i = 0, _a = authors.authors; _i < _a.length; _i++) {
            var author = _a[_i];
            $('#authors').append($('<li>').append(author.lastName + ", " + author.firstName));
        }
    }
    return {
    	'start': start
    }
});
