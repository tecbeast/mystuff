import { Country } from './Country';

export class Author {

    id: string;
    url: string;
    firstName: string;
    lastName: string;
    country: Country;

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.url = data.url;
            this.firstName = data.firstName;
            this.lastName = data.lastName;
            this.country = new Country(data.country);
        }
    }

}

export class Authors {

    authors: Author[];

    constructor(data?: any) {
        this.clear();
        if (data) {
            for (let authorData of data.authors) {
                this.add(new Author(authorData));
            }
        }
    }

    add(author: Author): void {
        this.authors.push(author);
    }

    clear(): void {
        this.authors = [];
    }

    static findAll(callback: Function): void {
    	console.log('findAll');
    	$.ajax({
	    	type: 'GET',
		    url: 'http://localhost:8080/mystuff/rest/authors/',
		    dataType: "json",
		    success: function(data) {
          callback(new Authors(data));
        }
	    });
    }

}
