angular.module('app').controller('ordersController', function ($scope, $http, $localStorage) {
    const contextPath = 'http://localhost:8189/summer';

    $scope.loadOrders = function () {
        $http({
            url: contextPath + '/api/v1/orders',
            method: 'GET'
        }).then(function (response) {
            $scope.orders = response.data;
            console.log($scope.orders);
        });
    }

    paypal.Buttons({
        createOrder: function(data, actions) {
            return actions.order.create({
                purchase_units: [{
                    amount: {
                        currency: 'US',
                        value: '100.00'
                    }
                }]
            });
        },
        onApprove: function(data, actions) {
            return actions.order.capture().then(function(details) {
                alert('Transaction completed by ' + details.payer.name.given_name);
            });
        },
        onCancel: function (data) {
            alert('Cancel');

        },
        onError: function (err) {
            alert('Error');

        }
    }).render('#paypal-button-container');

    $scope.loadOrders();
});