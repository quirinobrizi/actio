angular.module('actio-modeler')
  .config(['$locationProvider', '$routeProvider', 'growlProvider',
    function config($locationProvider, $routeProvider, growlProvider) {
      growlProvider.globalTimeToLive(10000);
      $locationProvider.hashPrefix('!');
      $routeProvider.
      	when('/login', {
      	  templateUrl: 'js/core/authentication/template.html'
        }).
        when('/dashboard', {
          template: '<dashboard></dashboard>'
        }).
        when('/models', {
            template: '<models></models>'
          }).
        when('/editor', {
            template: '<editor></editor>'
          }).
        when('/editor/:modelKey', {
            template: '<editor></editor>'
          }).
        otherwise('/login');
    }
  ])
  .config(['$httpProvider', function($httpProvider) {  
    $httpProvider.interceptors.push('AuthenticationInterceptor');
}]);