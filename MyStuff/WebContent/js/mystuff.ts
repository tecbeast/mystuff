/// <reference path="./lib/jquery.d.ts" />

import { Author, Authors } from './authors';

export function start(): void {
	new Authors().findAll(onSuccess);
}

function onSuccess(authors: Authors): void {
	for (var author of authors.authors) {
		$('#authors').append(
			$('<li>').append(
				author.lastName + ", " + author.firstName
			)
		);
	}
}