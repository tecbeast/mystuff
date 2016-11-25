define(["require", "exports", './overview'], function (require, exports, overview_1) {
    "use strict";
    function start() {
        new overview_1.Overview().update();
    }
    exports.start = start;
});
