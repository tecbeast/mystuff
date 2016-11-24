export class Country {
    
    code: string;
    name: string;

    constructor(data?: any) {
        this.init(data);
    }

    init(data?: any) : Country {
        this.code = (data && data.code) ? data.code : null;
        this.name = (data && data.name) ? data.name : null;
        return this;
    }

}
