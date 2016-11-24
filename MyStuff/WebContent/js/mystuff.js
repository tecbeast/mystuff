define(["require", "exports", './games'], function (require, exports, games_1) {
    "use strict";
    function start() {
        new games_1.Games().findAll(onSuccess);
    }
    exports.start = start;
    function onSuccess(games) {
        for (var _i = 0, _a = games.games; _i < _a.length; _i++) {
            var game = _a[_i];
            var author = game.authors.authors[0];
            $('#games').append($('<li>').append(game.name + ': '
                + author.lastName + ", " + author.firstName));
        }
    }
});
