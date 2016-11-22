/// <reference path="./lib/jquery.d.ts" />

import { Author, Authors } from './Authors';

export function start(): void {
	Authors.findAll(callback);
}

function callback(authors: Authors): void {
	for (let author of authors.authors) {
		$('#authors').append(
			$('<li>').append(
				author.lastName + ", " + author.firstName
			)
		);
	}
}