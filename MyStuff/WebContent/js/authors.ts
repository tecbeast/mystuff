import * as Rest from './rest';
import { Country } from './countries';

const PATH = 'authors/';

export class Author implements Rest.RestData {

    id: string;
    href: string;
    firstName: string;
    lastName: string;
    country: Country;

    constructor(data?: any) {
        this.init(data);
    }

    init(data?: any): Author {
        this.id = (data && data.id) ? data.id : null;
        this.href = (data && data.link && data.link.href) ? data.link.href : null;
        this.firstName = (data && data.firstName) ? data.firstName : null;
        this.lastName = (data && data.lastName) ? data.lastName : null;
        this.country = (data && data.country) ? new Country(data.country) : null;
        return this;
    }

    get fullName() {
        return this.firstName + ' ' + this.lastName;
    }

}

export class Authors implements Rest.RestData {

    authors: Author[];

    constructor(data?: any) {
        this.init(data);
    }

    init(data?: any): Authors {
        this.authors = [];
        if (data && data.authors) {
            for (var authorData of data.authors) {
                this.authors.push(new Author(authorData));
            }
        }
        return this;
    }

    get fullNames(): string[] {
        var fullNames = [];
        for (var author of this.authors) {
            fullNames.push(author.fullName);            
        }
        return fullNames;
    }

    findAll(onSuccess: Rest.OnSuccess): void {
        console.log('findAllAuthors()');
        Rest.get(PATH, this, onSuccess);
    }

}
