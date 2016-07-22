
        var app = angular.module("demoapp", ['ui-leaflet']);
        app.controller("LayersSimpleController", [ "$scope", function($scope) {
            angular.extend($scope, {
                center: {
                    lat: 25.0391667,
                    lng: 121.525,
                    zoom: 6
                },
                markers: {
                    taipei: {
                        lat: 25.0391667,
                        lng: 121.525,
                    }
                },
                layers: {
                    baselayers: {
                        osm: {
                            name: 'OpenStreetMap',
                            url: 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                            type: 'xyz'
                        }
                    }
                }
            });
        }]);
