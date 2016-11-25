define(["require", "exports", './rest', './authors', './publishers', './images', './awards', './notes'], function (require, exports, Rest, authors_1, publishers_1, images_1, awards_1, notes_1) {
    "use strict";
    var PATH = 'games/';
    var Game = (function () {
        function Game(data) {
            this.init(data);
        }
        Game.prototype.init = function (data) {
            this.id = (data && data.id) ? data.id : null;
            this.href = (data && data.link && data.link.href) ? data.link.href : null;
            this.title = (data && data.title) ? data.title : null;
            this.subtitle = (data && data.subtitle) ? data.subtitle : null;
            this.publishedYear = (data && data.publishedYear) ? data.publishedYear : null;
            this.playersMin = (data && data.playersMin) ? data.playersMin : 0;
            this.playersMax = (data && data.playersMax) ? data.playersMax : 0;
            this.playtimeMin = (data && data.playtimeMin) ? data.playtimeMin : 0;
            this.playtimeMax = (data && data.playtimeMax) ? data.playtimeMax : 0;
            this.playtimePerPlayer = (data && data.playtimePerPlayer) ? data.playtimePerPlayer : false;
            this.ageMin = (data && data.ageMin) ? data.ageMin : 0;
            this.description = (data && data.description) ? data.description : null;
            this.rating = (data && data.rating) ? data.rating : 0;
            this.authors = (data && data.authors) ? new authors_1.Authors(data.authors) : new authors_1.Authors();
            this.publishers = (data && data.publishers) ? new publishers_1.Publishers(data.publishers) : new publishers_1.Publishers();
            this.images = (data && data.images) ? new images_1.Images(data.images) : new images_1.Images();
            this.awards = (data && data.awards) ? new awards_1.Awards(data.awards) : new awards_1.Awards();
            this.notes = (data && data.notes) ? new notes_1.Notes(data.notes) : new notes_1.Notes();
            return this;
        };
        return Game;
    }());
    exports.Game = Game;
    var Games = (function () {
        function Games(data) {
            this.init(data);
        }
        Games.prototype.init = function (data) {
            this.games = [];
            if (data && data.games) {
                for (var _i = 0, _a = data.games; _i < _a.length; _i++) {
                    var gameData = _a[_i];
                    this.games.push(new Game(gameData));
                }
            }
            return this;
        };
        Games.prototype.findAll = function (onSuccess) {
            console.log('findAllGames()');
            Rest.get(PATH, this, onSuccess);
        };
        return Games;
    }());
    exports.Games = Games;
});
