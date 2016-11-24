import * as Rest from './rest';
import { Country } from './countries';

const PATH = 'publishers/';

export class Publisher implements Rest.RestData {

    id: string;
    href: string;
    name: string;
    country: Country;

    constructor(data?: any) {
        this.init(data);
    }

    init(data?: any): Publisher {
        this.id = (data && data.id) ? data.id : null;
        this.href = (data && data.link && data.link.href) ? data.link.href : null;
        this.name = (data && data.name) ? data.name : null;
        this.country = (data && data.country) ? new Country(data.country) : null;
        return this;
    }

}

export class Publishers implements Rest.RestData {

    publishers: Publisher[];

    constructor(data?: any) {
        this.init(data);
    }

    init(data?: any): Publishers {
        this.publishers = [];
        if (data && data.publishers) {
            for (var publisherData of data.publishers) {
                this.publishers.push(new Publisher(publisherData));
            }
        }
        return this;
    }

    findAll(onSuccess: Rest.OnSuccess): void {
        console.log('findAllPublishers()');
        Rest.get(PATH, this, onSuccess);
    }

}
