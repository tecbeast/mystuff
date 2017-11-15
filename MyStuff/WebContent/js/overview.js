define(["require", "exports", 'handlebars', './games'], function (require, exports, Handlebars, games_1) {
    "use strict";
    var Overview = (function () {
        function Overview() {
            this.games = new games_1.Games();
        }
        Overview.prototype.update = function () {
            this.games.findAll(this.render.bind(this));
        };
        Overview.prototype.render = function () {
            $('#games').empty();
            var templateScript = $('#overview-template').html();
            var template = Handlebars.compile(templateScript);
            for (var _i = 0, _a = this.games.games; _i < _a.length; _i++) {
                var game = _a[_i];
                $('#games').append(template(game));
            }
        };
        return Overview;
    }());
    exports.Overview = Overview;
});
