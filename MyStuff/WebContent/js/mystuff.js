define(["require", "exports", './authors'], function (require, exports, authors_1) {
    "use strict";
    function start() {
        new authors_1.Authors().findAll(onSuccess);
    }
    exports.start = start;
    function onSuccess(authors) {
        for (var _i = 0, _a = authors.authors; _i < _a.length; _i++) {
            var author = _a[_i];
            $('#authors').append($('<li>').append(author.lastName + ", " + author.firstName));
        }
    }
});
