'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', [
  'ngRoute',
  'ngResource',
  'myApp.filters',
  'myApp.services',
  'myApp.directives',
  'myApp.controllers'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/jobs', {templateUrl: 'partials/jobs.html', controller: 'JobsCtrl'});
  $routeProvider.otherwise({redirectTo: '/jobs'});
}]);
