angular.module('actio-modeler')
  .config(['$locationProvider', '$routeProvider', 'growlProvider',
    function config($locationProvider, $routeProvider, growlProvider) {
      growlProvider.globalTimeToLive(10000);
      $locationProvider.hashPrefix('!');
      $routeProvider.
        when('/dashboard', {
          template: '<dashboard></dashboard>'
        }).
        when('/models', {
            template: '<models></models>'
          }).
        when('/editor', {
            template: '<editor></editor>'
          }).
        otherwise('/dashboard');
    }
  ]);