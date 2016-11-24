import * as Rest from './rest';

const PATH = 'notes/';

export class Note implements Rest.RestData {

    id: string;
    href: string;
    timestamp: string;
    text: string;

    constructor(data?: any) {
        this.init(data);
    }

    init(data?: any): Note {
        this.id = (data && data.id) ? data.id : null;
        this.href = (data && data.link && data.link.href) ? data.link.href : null;
        this.timestamp = (data && data.timestamp) ? data.timestamp : null;
        this.text = (data && data.text) ? data.text : null;
        return this;
    }

}

export class Notes implements Rest.RestData {

    notes: Note[];

    constructor(data?: any) {
        this.init(data);
    }

    init(data?: any): Notes {
        this.notes = [];
        if (data && data.notes) {
            for (var noteData of data.notes) {
                this.notes.push(new Note(noteData));
            }
        }
        return this;
    }

    findAll(onSuccess: Rest.OnSuccess): void {
        console.log('findAllNotes()');
        Rest.get(PATH, this, onSuccess);
    }

}
