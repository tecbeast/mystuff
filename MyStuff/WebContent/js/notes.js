define(["require", "exports", './rest'], function (require, exports, Rest) {
    "use strict";
    var PATH = 'notes/';
    var Note = (function () {
        function Note(data) {
            this.init(data);
        }
        Note.prototype.init = function (data) {
            this.id = (data && data.id) ? data.id : null;
            this.href = (data && data.link && data.link.href) ? data.link.href : null;
            this.timestamp = (data && data.timestamp) ? data.timestamp : null;
            this.text = (data && data.text) ? data.text : null;
            return this;
        };
        return Note;
    }());
    exports.Note = Note;
    var Notes = (function () {
        function Notes(data) {
            this.init(data);
        }
        Notes.prototype.init = function (data) {
            this.notes = [];
            if (data && data.notes) {
                for (var _i = 0, _a = data.notes; _i < _a.length; _i++) {
                    var noteData = _a[_i];
                    this.notes.push(new Note(noteData));
                }
            }
            return this;
        };
        Notes.prototype.findAll = function (onSuccess) {
            console.log('findAllNotes()');
            Rest.get(PATH, this, onSuccess);
        };
        return Notes;
    }());
    exports.Notes = Notes;
});
