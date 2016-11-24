define(["require", "exports"], function (require, exports) {
    "use strict";
    var BASE_URL = 'http://localhost:8080/mystuff/rest/';
    function get(path, restData, onSuccess) {
        $.ajax({
            type: 'GET',
            url: BASE_URL + path,
            dataType: "json",
            success: function (jsonData) {
                onSuccess(restData.init(jsonData));
            }
        });
    }
    exports.get = get;
});
