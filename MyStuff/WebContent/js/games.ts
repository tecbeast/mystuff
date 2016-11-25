import * as Rest from './rest';
import { Authors } from './authors';
import { Publishers } from './publishers';
import { Images } from './images';
import { Awards } from './awards';
import { Notes } from './notes';

const PATH = 'games/';

export class Game implements Rest.RestData {

    id: string;
    href: string;
    title: string;
    subtitle: string;
    publishedYear: number;
    playersMin: number;
    playersMax: number;
    playtimeMin: number;
    playtimeMax: number;
    playtimePerPlayer: boolean;
    ageMin: number;
    description: string;
    rating: number;
    authors: Authors;
    publishers: Publishers;
    images: Images;
    awards: Awards;
    notes: Notes;

    constructor(data?: any) {
        this.init(data);
    }

    init(data?: any): Game {
        this.id = (data && data.id) ? data.id : null;
        this.href = (data && data.link && data.link.href) ? data.link.href : null;
        this.title = (data && data.title) ? data.title : null;
        this.subtitle = (data && data.subtitle) ? data.subtitle : null;
        this.publishedYear = (data && data.publishedYear) ? data.publishedYear : null;
        this.playersMin = (data && data.playersMin) ? data.playersMin : 0;
        this.playersMax = (data && data.playersMax) ? data.playersMax : 0;
        this.playtimeMin = (data && data.playtimeMin) ? data.playtimeMin : 0;
        this.playtimeMax = (data && data.playtimeMax) ? data.playtimeMax : 0;
        this.playtimePerPlayer = (data && data.playtimePerPlayer) ? data.playtimePerPlayer : false;
        this.ageMin = (data && data.ageMin) ? data.ageMin : 0;
        this.description = (data && data.description) ? data.description : null;
        this.rating = (data && data.rating) ? data.rating : 0;
        this.authors = (data && data.authors) ? new Authors(data.authors) : new Authors();
        this.publishers = (data && data.publishers) ? new Publishers(data.publishers) : new Publishers();
        this.images = (data && data.images) ? new Images(data.images) : new Images();
        this.awards = (data && data.awards) ? new Awards(data.awards) : new Awards();
        this.notes = (data && data.notes) ? new Notes(data.notes) : new Notes();
        return this;
    }

}

export class Games implements Rest.RestData {

    games: Game[];

    constructor(data?: any) {
        this.init(data);
    }

    init(data?: any): Games {
        this.games = [];
        if (data && data.games) {
            for (var gameData of data.games) {
                this.games.push(new Game(gameData));
            }
        }
        return this;
    }

    findAll(onSuccess: Rest.OnSuccess): void {
        console.log('findAllGames()');
        Rest.get(PATH, this, onSuccess);
    }

}