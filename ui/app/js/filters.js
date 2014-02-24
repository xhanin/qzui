'use strict';

/* Filters */

angular.module('myApp.filters', []).
    filter('jobs', function ($filter) {
        return function (jobs, text, groups) {
            if (text && text != "") {
                return $filter('filter')(jobs, text);
            } else if (_.isObject(groups)) {
                return $filter('filter')(jobs, function (job) {
                    return groups[job.group] === true;
                });
            } else {
                return jobs;
            }
        }
    });
