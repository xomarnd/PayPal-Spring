(function ($localStorage) {
    'use strict';

    angular
        .module('app', ['ngRoute', 'ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'greetings/greetings.html',
                controller: 'greetingsController'
            })
            .when('/products', {
                templateUrl: 'products/products.html',
                controller: 'productsController'
            })
            .when('/cart', {
                templateUrl: 'cart/cart.html',
                controller: 'cartController'
            })
            .when('/orders', {
                templateUrl: 'orders/orders.html',
                controller: 'ordersController'
            })
            .otherwise({
                redirectTo: '/'
            });
    }

    function run($rootScope, $http, $localStorage) {
        const contextPath = 'http://localhost:8189/summer/api/v1';

        if ($localStorage.summerUser) {
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.summerUser.token;
        }
        if (!$localStorage.guestCartUuid) {
            $http.get(contextPath + '/cart/generate')
                .then(function successCallback(response) {
                    $localStorage.guestCartUuid = response.data.value;
                });
        }
    }
})();

angular.module('app').controller('indexController', function ($rootScope, $location, $scope, $http, $localStorage) {
    const contextPath = 'http://localhost:8189/summer/api/v1';

    $scope.tryToAuth = function () {
        $http.post(contextPath + '/auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $location.path('/products');

                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.summerUser = {username: $scope.user.username, token: response.data.token};

                    if ($scope.user.username) {
                        $scope.user.username = null;
                    }
                    if ($scope.user.password) {
                        $scope.user.password = null;
                    }

                    $http.get(contextPath + '/cart/' + $localStorage.guestCartUuid + '/merge')
                        .then(function successCallback(response) {
                        });
                }
            }, function errorCallback(response) {
            });
    };

    $scope.tryToLogout = function () {
        $location.path('/products');
        $scope.clearUser();
        if ($scope.user.username) {
            $scope.user.username = null;
        }
        if ($scope.user.password) {
            $scope.user.password = null;
        }
    };

    $scope.clearUser = function () {
        delete $localStorage.summerUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $rootScope.isUserLoggedIn = function () {
        if ($localStorage.summerUser) {
            return true;
        } else {
            return false;
        }
    };
});