define(["require", "exports", './rest', './countries'], function (require, exports, Rest, countries_1) {
    "use strict";
    var PATH = 'awards/';
    var Award = (function () {
        function Award(data) {
            this.init(data);
        }
        Award.prototype.init = function (data) {
            this.id = (data && data.id) ? data.id : null;
            this.href = (data && data.link && data.link.self) ? data.link.self : null;
            this.name = (data && data.name) ? data.name : null;
            this.year = (data && data.year) ? data.year : 0;
            this.country = (data && data.country) ? new countries_1.Country(data.country) : null;
            return this;
        };
        return Award;
    }());
    exports.Award = Award;
    var Awards = (function () {
        function Awards(data) {
            this.init(data);
        }
        Awards.prototype.init = function (data) {
            this.awards = [];
            if (data && data.awards) {
                for (var _i = 0, _a = data.awards; _i < _a.length; _i++) {
                    var awardData = _a[_i];
                    this.awards.push(new Award(awardData));
                }
            }
            return this;
        };
        Awards.prototype.findAll = function (onSuccess) {
            console.log('findAllAwards()');
            Rest.get(PATH, this, onSuccess);
        };
        return Awards;
    }());
    exports.Awards = Awards;
});
