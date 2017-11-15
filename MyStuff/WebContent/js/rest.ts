/// <reference path="./lib/jquery.d.ts" />

const BASE_URL: string = 'http://localhost:8080/mystuff/rest/';

export interface RestData {
    init(data?: any): RestData;
}

export interface OnSuccess {
    (RestData): void;
}

export function get(path: string, restData: RestData, onSuccess: OnSuccess) {
    $.ajax({
        type: 'GET',
        url: BASE_URL + path,
        dataType: "json",
        success: function (jsonData) {
            onSuccess(restData.init(jsonData));
        }
    });
}
