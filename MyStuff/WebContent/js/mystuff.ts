/// <reference path="./lib/jquery.d.ts" />

import { Game, Games } from './games';

export function start(): void {
	new Games().findAll(onSuccess);
}

function onSuccess(games: Games): void {
	for (var game of games.games) {		
		var author = game.authors.authors[0];
		$('#games').append(
			$('<li>').append(
				game.name + ': '
				+ author.lastName + ", " + author.firstName
			)
		);
	}
}