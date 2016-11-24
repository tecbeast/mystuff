define(["require", "exports", './config', './countries'], function (require, exports, Config, countries_1) {
    "use strict";
    var Author = (function () {
        function Author(data) {
            this.init(data);
        }
        Author.prototype.init = function (data) {
            this.id = (data && data.id) ? data.id : null;
            this.url = (data && data.utl) ? data.url : null;
            this.firstName = (data && data.firstName) ? data.firstName : null;
            this.lastName = (data && data.lastName) ? data.lastName : null;
            this.country = (data && data.country) ? new countries_1.Country(data.country) : null;
            return this;
        };
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
        Authors.prototype.findAll = function (callback) {
            console.log('findAll');
            $.ajax({
                type: 'GET',
                url: Config.BASE_URL + 'authors/',
                dataType: "json",
                success: function (data) {
                    callback(this.init(data));
                }.bind(this)
            });
        };
        return Authors;
    }());
    exports.Authors = Authors;
});
