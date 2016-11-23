define(["require", "exports"], function (require, exports) {
    "use strict";
    var Country = (function () {
        function Country(data) {
            if (data) {
                this.code = data.code;
                this.name = data.name;
            }
        }
        return Country;
    }());
    exports.Country = Country;
});
