define(["require", "exports", './rest'], function (require, exports, Rest) {
    "use strict";
    var PATH = 'images/';
    var Image = (function () {
        function Image(data) {
            this.init(data);
        }
        Image.prototype.init = function (data) {
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
        };
        return Image;
    }());
    exports.Image = Image;
    var Images = (function () {
        function Images(data) {
            this.init(data);
        }
        Images.prototype.init = function (data) {
            this.images = [];
            if (data && data.images) {
                for (var _i = 0, _a = data.images; _i < _a.length; _i++) {
                    var imageData = _a[_i];
                    this.images.push(new Image(imageData));
                }
            }
            return this;
        };
        Images.prototype.findAll = function (onSuccess) {
            console.log('findAllImages()');
            Rest.get(PATH, this, onSuccess);
        };
        return Images;
    }());
    exports.Images = Images;
});
