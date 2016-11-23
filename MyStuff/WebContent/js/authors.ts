import * as Config from './config';
import { Country } from './countries';

export class Author {

    id: string;
    url: string;
    firstName: string;
    lastName: string;
    country: Country;

    constructor(data?: any) {
        this.init(data);
    }

    init(data?: any): Author {
        this.id = (data && data.id) ? data.id : null;
        this.url = (data && data.utl) ? data.url : null;
        this.firstName = (data && data.firstName) ? data.firstName : null;
        this.lastName = (data && data.lastName) ? data.lastName : null;
        this.country = (data && data.country) ? new Country(data.country) : null;
        return this;
    }

}

export class Authors {

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

    findAll(callback: Function): void {
        console.log('findAll');
        $.ajax({
            type: 'GET',
            url: Config.BASE_URL + 'authors/',
            dataType: "json",
            success: function (data) {
                callback(this.init(data));
            }
        });
    }

}
