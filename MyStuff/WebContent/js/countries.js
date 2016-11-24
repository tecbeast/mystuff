define(["require", "exports"], function (require, exports) {
    "use strict";
    var Country = (function () {
        function Country(data) {
            this.init(data);
        }
        Country.prototype.init = function (data) {
            this.code = (data && data.code) ? data.code : null;
            this.name = (data && data.name) ? data.name : null;
            return this;
        };
        return Country;
    }());
    exports.Country = Country;
});
