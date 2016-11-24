export class Country {
    
    code: string;
    name: string;

    constructor(data?: any) {
        if (data) {
            this.code = data.code;
            this.name = data.name;
        }
    }

}
