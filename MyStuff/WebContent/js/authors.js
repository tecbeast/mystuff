define(["require", "exports", './rest', './countries'], function (require, exports, Rest, countries_1) {
    "use strict";
    var PATH = 'authors/';
    var Author = (function () {
        function Author(data) {
            this.init(data);
        }
        Author.prototype.init = function (data) {
            this.id = (data && data.id) ? data.id : null;
            this.href = (data && data.link && data.link.href) ? data.link.href : null;
            this.firstName = (data && data.firstName) ? data.firstName : null;
            this.lastName = (data && data.lastName) ? data.lastName : null;
            this.country = (data && data.country) ? new countries_1.Country(data.country) : null;
            return this;
        };
        Object.defineProperty(Author.prototype, "fullName", {
            get: function () {
                return this.firstName + ' ' + this.lastName;
            },
            enumerable: true,
            configurable: true
        });
        return Author;
    }());
    exports.Author = Author;
    var Authors = (function () {
        function Authors(data) {
            this.init(data);
        }
        Authors.prototype.init = function (data) {
            this.authors = [];
            if (data && data.authors) {
                for (var _i = 0, _a = data.authors; _i < _a.length; _i++) {
                    var authorData = _a[_i];
                    this.authors.push(new Author(authorData));
                }
            }
            return this;
        };
        Object.defineProperty(Authors.prototype, "fullNames", {
            get: function () {
                var fullNames = [];
                for (var _i = 0, _a = this.authors; _i < _a.length; _i++) {
                    var author = _a[_i];
                    fullNames.push(author.fullName);
                }
                return fullNames;
            },
            enumerable: true,
            configurable: true
        });
        Authors.prototype.findAll = function (onSuccess) {
            console.log('findAllAuthors()');
            Rest.get(PATH, this, onSuccess);
        };
        return Authors;
    }());
    exports.Authors = Authors;
});
