define(function () {
    'use strict';
    function Note(data) {
    	this.init(data);
    };
    Note.prototype.init = function (data) {
		this.id = (data && data.id) ? data.id : null;
        this.href = (data && data.link && data.link.self) ? data.link.self : null;
        this.timestamp = (data && data.timestamp) ? data.timestamp : null;
        this.text = (data && data.text) ? data.text : null;
        return this;
    };
    return Note;
});
