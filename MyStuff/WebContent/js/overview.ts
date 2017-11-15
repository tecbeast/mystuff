/// <reference path="./lib/handlebars.d.ts" />

import Handlebars = require('handlebars');
import { Game, Games } from './games';

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
            $('#games').append(template(game));
	    }
    }

}

