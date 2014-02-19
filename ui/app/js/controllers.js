'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
  controller('JobsCtrl', ['$scope', '$http', 'Jobs', function($scope, $http, Jobs) {
        $scope.jobs = [];

        function loadJobs() {
            $http.get('/api/jobs').success(function(jobs) {
                $scope.jobs = jobs;

                angular.forEach($scope.jobs, function(job) {
                    $http.get('/api/groups/' + job.group + '/jobs/' + job.name).success(function(j) {
                        job.triggers = j.triggers;
                    });
                });
            });
        }
        loadJobs();

        $scope.delete = function(job) {
            if (confirm('Are you sure you want to delete this job?')) {
                $http.delete('/api/groups/' + job.group + '/jobs/' + job.name).success(function() {
                    alert('job deleted');

                });
            }
        }
  }])
  .controller('HeaderController', ['$scope', '$location', function($scope, $location) {
        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };
  }])
;