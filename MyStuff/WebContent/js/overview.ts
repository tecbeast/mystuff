/// <reference path="./lib/handlebars.d.ts" />

import Handlebars = require('handlebars');
import { Game, Games } from './games';

const RATING_CLASSES = [
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
]

export class Overview {

    games: Games;

    constructor() {
        this.games = new Games();
    }

    update(): void {
        this.games.findAll(this.render.bind(this));
    }

    render(): void {
        $('#games').empty();
        var templateScript = $('#overview-template').html();
        var template = Handlebars.compile(templateScript);
        for (var game of this.games.games) {
            var data = new OverviewData(game);
            $('#games').append(template(data));
	    }
    }

}

class OverviewData {

    game: Game;

    constructor(game: Game) {
        this.game = game;
    }

    get coverUrl() {
        for (var image of this.game.images.images) {
            if (image.role === 'overview') {
                return image.url;
            }
        }
        return location.protocol + '//' + location.host + '/' + location.pathname + '/icons/empty_cover.png';
    }

    get title() {
        return this.game.title;
    }

    get subtitle() {
        return this.game.subtitle;
    }

    get players() {
        return this.game.playersMin + ' - ' + this.game.playersMax + ' Spieler';
    }

    get playtime() {
        if (this.game.playtimeMax) {
            return this.game.playtimeMin + ' - ' + this.game.playtimeMax + ' Min';
        } else {
            return this.game.playtimeMin + '+ Min'
        }
    }

    get age() {
        return this.game.ageMin + '+ Jahre';
    }

    get publishers() {
        var publisherNames = [];
        for (var publisher of this.game.publishers.publishers) {
            publisherNames.push(publisher.name);            
        }
        return publisherNames.join(', ');
    }

    get authors() {
        var authorNames = [];
        for (var author of this.game.authors.authors) {
            authorNames.push(author.fullName);            
        }
        return authorNames.join(', ');
    }

    get awards() {
        var awardNames = [];
        for (var award of this.game.awards.awards) {
            awardNames.push(award.fullName);            
        }
        return awardNames.join(', ');
    }

    get rating() {
        return '';
    }

    get ratingClass() {
        return '';
    }

}
