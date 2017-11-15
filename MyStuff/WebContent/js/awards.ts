import * as Rest from './rest';
import { Country } from './countries';

const PATH = 'awards/';

export class Award implements Rest.RestData {

    id: string;
    href: string;
    name: string;
    year: number;
    country: Country;

    constructor(data?: any) {
        this.init(data);
    }

    init(data?: any): Award {
		this.id = (data && data.id) ? data.id : null;
        this.href = (data && data.link && data.link.self) ? data.link.self : null;
        this.name = (data && data.name) ? data.name : null;
        this.year = (data && data.year) ? data.year : 0;
        this.country = (data && data.country) ? new Country(data.country) : null;
    	return this;
    }

    get fullName() {
        if (this.year) {
            return this.name + ' ' + this.year;
        } else {
            return this.name;
        }
    }

}

export class Awards implements Rest.RestData {

    awards: Award[];

    constructor(data?: any) {
        this.init(data);
    }

    init(data?: any): Awards {
        this.awards = [];
        if (data && data.awards) {
            for (var awardData of data.awards) {
                this.awards.push(new Award(awardData));
            }
        }
        return this;
    }

    get fullNames(): string[] {
        var fullNames = [];
        for (var award of this.awards) {
            fullNames.push(award.fullName);            
        }
        return fullNames;
    }

    findAll(onSuccess: Rest.OnSuccess): void {
        console.log('findAllAwards()');
        Rest.get(PATH, this, onSuccess);
    }

}
