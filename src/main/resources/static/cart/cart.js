angular.module('app').controller('cartController', function ($scope, $http, $localStorage) {
    const contextPath = 'http://localhost:8189/summer';

    $scope.loadCart = function () {
        $http({
            url: contextPath + '/api/v1/cart/' + $localStorage.guestCartUuid,
            method: 'GET'
        }).then(function (response) {
            $scope.cart = response.data;
        });
    }

    $scope.addToCart = function (productId) {
        $http({
            url: contextPath + '/api/v1/cart/' + $localStorage.guestCartUuid + '/add/' + productId,
            method: 'GET'
        }).then(function (response) {
            $scope.loadCart();
        });
    }

    $scope.incrementCartPosition = function (productId) {
        $http({
            url: contextPath + '/api/v1/cart/' + $localStorage.guestCartUuid + '/add/' + productId,
            method: 'GET'
        }).then(function (response) {
            $scope.loadCart();
        });
    }

    $scope.decrementCartPosition = function (productId) {
        $http({
            url: contextPath + '/api/v1/cart/' + $localStorage.guestCartUuid + '/decrement/' + productId,
            method: 'GET'
        }).then(function (response) {
            $scope.loadCart();
        });
    }

    $scope.clearCart = function () {
        $http({
            url: contextPath + '/api/v1/cart/' + $localStorage.guestCartUuid + '/clear',
            method: 'GET'
        }).then(function (response) {
            $scope.cart = null;
        });
    }

    $scope.removeItemFromCart = function (productId) {
        $http({
            url: contextPath + '/api/v1/cart/' + $localStorage.guestCartUuid + '/remove/' + productId,
            method: 'GET'
        }).then(function (response) {
            $scope.loadCart();
        });
    }

    $scope.createOrder = function () {
        $http({
            url: contextPath + '/api/v1/orders',
            method: 'POST',
            params: {
                phone: $scope.order_info.phone,
                address: $scope.order_info.address
            }
        }).then(function successCallback(response) {
            alert('Заказ создан');
            $scope.loadCart();
        }, function errorCallback(response) {
            alert(response.data.messages);
        });
    }

    $scope.loadCart();
    console.log("cart page loaded");
});