import * as Rest from './rest';

const PATH = 'images/';

export class Image implements Rest.RestData {

    id: string;
    href: string;
    role: string;
    width: number;
    height: number;
    url: string;
    description: string;

    constructor(data?: any) {
        this.init(data);
    }

    init(data?: any): Image {
        this.id = (data && data.id) ? data.id : null;
        this.href = (data && data.link && data.link.href) ? data.link.href : null;
        this.role = (data && data.role) ? data.role : null;
        this.width = (data && data.width) ? data.width : 0;
        this.height = (data && data.height) ? data.height : 0;
        this.url = (data && data.url) ? data.url : null;
        if ((this.url) && (this.url.charAt(0) === '/')) {
            this.url = location.protocol + '//' + location.host + '/' + location.pathname + this.url;
        }
        this.description = (data && data.description) ? data.description : null;
        return this;
    }

}

export class Images implements Rest.RestData {

    images: Image[];

    constructor(data?: any) {
        this.init(data);
    }

    init(data?: any): Images {
        this.images = [];
        if (data && data.images) {
            for (var imageData of data.images) {
                this.images.push(new Image(imageData));
            }
        }
        return this;
    }

    get coverUrl() {
        for (var image of this.images) {
            if (image.role === 'overview') {
                return image.url;
            }
        }
        return location.protocol + '//' + location.host + '/' + location.pathname + '/icons/empty_cover.png';
    }

    findAll(onSuccess: Rest.OnSuccess): void {
        console.log('findAllImages()');
        Rest.get(PATH, this, onSuccess);
    }

}
