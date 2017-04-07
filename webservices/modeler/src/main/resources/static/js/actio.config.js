angular.module('actio-modeler')
  .config(['$locationProvider', '$routeProvider', 'growlProvider',
    function config($locationProvider, $routeProvider, growlProvider) {
      growlProvider.globalTimeToLive(10000);
      $locationProvider.hashPrefix('!');
      $routeProvider.
      	when('/login', {
      	  template: '<authentication></authentication>'
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
  .service('TransactionInterceptor', ['$q', '$location', 'Auth', function($q, $location, Auth) {
		this.request = function(config) {
			console.log('AuthenticationInterceptor - intercepted request');
			if(Auth.isAuthenticated()) {
				config.headers['X-Actio-Username'] = Auth.details().userName;
			}
			return config;
		};
		this.responseError = function(response) {
			if (response.status === 401 || response.status === 403) {
	            console.log('unauthorized');
	            Auth.clear();
	            $location.path('/login');
	            return $q.reject(response);;
	        } else if(response.status === 400 || response.status > 403) {
	        	console.log('generic error');
	        	$location.path('/error');
	        	return $q.reject(response);
	        }
			return $q.reject(response);
		};
		this.response = function(response) {
			return response || $q.when(response);
	    };
  }])
  .config(['$httpProvider', 'localStorageServiceProvider', function($httpProvider, localStorageServiceProvider) {
	localStorageServiceProvider.setPrefix('actio');
	$httpProvider.interceptors.push('TransactionInterceptor');
  }])
  .run(['$location', 'Bus', 'Auth', function($location, Bus, Auth){
	  Bus.listen('$locationChangeStart', function(event, next, current) {
		  if($location.path() !== '/login') {
			  if(!Auth.isAuthenticated()) {
				  event.preventDefault();
				  $location.path('/login');
				  return;
			  }
		  }
		  Bus.emit('actio.login.successful', Auth.details());
	  });
	  
	  Bus.listen('actio.authenticated.user.cleared', function(e, data) {
		  event.preventDefault();
		  $location.path('/login');
		  return;
  	  });
  }]);