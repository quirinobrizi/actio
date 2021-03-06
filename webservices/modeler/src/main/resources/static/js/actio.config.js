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
  .service('TransactionInterceptor', ['$q', '$location', 'Auth', 'Bus', function($q, $location, Auth, Bus) {
		this.request = function(config) {
			if(Auth.isAuthenticated()) {
				config.headers['X-Actio-Username'] = Auth.details().user.userName;
			}
			return config;
		};
		this.responseError = function(response) {
			if (response.status === 401 || response.status === 403) {
	            Auth.clear();
	            $location.path('/login');
	            return $q.reject(response);;
	        } else if(response.status === 400 || response.status > 403) {
	        	Bus.emit('actio.runtime.error', response);
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
	  Bus.listen('$routeChangeStart', function(event, next, current) {
		  if($location.path() !== '/login' && !Auth.isAuthenticated()) {
			  Auth.clear();
		  }
	  });
	  
	  Bus.listen('actio.authentication.cleared', function(e, data) {
		  e.preventDefault();
		  $location.path('/login');
		  return;
  	  });
	  
	  Bus.listen('actio.runtime.error', function(e,data) {
		  console.log(data);
		  alert("Error: [" + (data.data.code || 500) + "] " + (data.data.message || "Generic Error"));
  	  });
  }]);