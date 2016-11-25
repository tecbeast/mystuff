define(["require", "exports", 'handlebars', './games'], function (require, exports, Handlebars, games_1) {
    "use strict";
    var RATING_CLASSES = [
        'rating00',
        'rating05',
        'rating10',
        'rating15',
        'rating20',
        'rating25',
        'rating30',
        'rating35',
        'rating40',
        'rating45',
        'rating50'
    ];
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
                var data = new OverviewData(game);
                $('#games').append(template(data));
            }
        };
        return Overview;
    }());
    exports.Overview = Overview;
    var OverviewData = (function () {
        function OverviewData(game) {
            this.game = game;
        }
        Object.defineProperty(OverviewData.prototype, "coverUrl", {
            get: function () {
                for (var _i = 0, _a = this.game.images.images; _i < _a.length; _i++) {
                    var image = _a[_i];
                    if (image.role === 'overview') {
                        return image.url;
                    }
                }
                return location.protocol + '//' + location.host + '/' + location.pathname + '/icons/empty_cover.png';
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverviewData.prototype, "title", {
            get: function () {
                return this.game.title;
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverviewData.prototype, "subtitle", {
            get: function () {
                return this.game.subtitle;
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverviewData.prototype, "players", {
            get: function () {
                return this.game.playersMin + ' - ' + this.game.playersMax + ' Spieler';
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverviewData.prototype, "playtime", {
            get: function () {
                if (this.game.playtimeMax) {
                    return this.game.playtimeMin + ' - ' + this.game.playtimeMax + ' Min';
                }
                else {
                    return this.game.playtimeMin + '+ Min';
                }
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverviewData.prototype, "age", {
            get: function () {
                return this.game.ageMin + '+ Jahre';
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverviewData.prototype, "publishers", {
            get: function () {
                var publisherNames = [];
                for (var _i = 0, _a = this.game.publishers.publishers; _i < _a.length; _i++) {
                    var publisher = _a[_i];
                    publisherNames.push(publisher.name);
                }
                return publisherNames.join(', ');
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverviewData.prototype, "authors", {
            get: function () {
                var authorNames = [];
                for (var _i = 0, _a = this.game.authors.authors; _i < _a.length; _i++) {
                    var author = _a[_i];
                    authorNames.push(author.fullName);
                }
                return authorNames.join(', ');
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverviewData.prototype, "awards", {
            get: function () {
                var awardNames = [];
                for (var _i = 0, _a = this.game.awards.awards; _i < _a.length; _i++) {
                    var award = _a[_i];
                    awardNames.push(award.fullName);
                }
                return awardNames.join(', ');
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverviewData.prototype, "rating", {
            get: function () {
                return '';
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverviewData.prototype, "ratingClass", {
            get: function () {
                return '';
            },
            enumerable: true,
            configurable: true
        });
        return OverviewData;
    }());
});
