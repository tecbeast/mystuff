define(["require", "exports", './rest', './countries'], function (require, exports, Rest, countries_1) {
    "use strict";
    var PATH = 'publishers/';
    var Publisher = (function () {
        function Publisher(data) {
            this.init(data);
        }
        Publisher.prototype.init = function (data) {
            this.id = (data && data.id) ? data.id : null;
            this.href = (data && data.link && data.link.href) ? data.link.href : null;
            this.name = (data && data.name) ? data.name : null;
            this.country = (data && data.country) ? new countries_1.Country(data.country) : null;
            return this;
        };
        return Publisher;
    }());
    exports.Publisher = Publisher;
    var Publishers = (function () {
        function Publishers(data) {
            this.init(data);
        }
        Publishers.prototype.init = function (data) {
            this.publishers = [];
            if (data && data.publishers) {
                for (var _i = 0, _a = data.publishers; _i < _a.length; _i++) {
                    var publisherData = _a[_i];
                    this.publishers.push(new Publisher(publisherData));
                }
            }
            return this;
        };
        Object.defineProperty(Publishers.prototype, "names", {
            get: function () {
                var names = [];
                for (var _i = 0, _a = this.publishers; _i < _a.length; _i++) {
                    var publisher = _a[_i];
                    names.push(publisher.name);
                }
                return names;
            },
            enumerable: true,
            configurable: true
        });
        Publishers.prototype.findAll = function (onSuccess) {
            console.log('findAllPublishers()');
            Rest.get(PATH, this, onSuccess);
        };
        return Publishers;
    }());
    exports.Publishers = Publishers;
});
