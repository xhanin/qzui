'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('myApp.services', [])
    .value('version', '0.1')
    .factory('Jobs', ['$resource', function($resource) {
        return $resource('/api/groups/:group/jobs/:name');
    }])
;
