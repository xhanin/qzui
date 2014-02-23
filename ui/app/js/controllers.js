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
        };

        $scope.isTriggered = function(trigger) {
            return trigger.when && moment().isAfter(moment(trigger.when));
        };

        var updateTime = function(){
            $scope.now = moment().toDate();
        };

        updateTime();

        window.setInterval(function() {
            $scope.$apply(updateTime);
        }, 1000);
  }])
  .controller('HeaderController', ['$scope', '$location', function($scope, $location) {
        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };
  }])
;