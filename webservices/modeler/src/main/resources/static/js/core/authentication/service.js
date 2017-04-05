/*******************************************************************************
 * Copyright [2016] [Quirino Brizi (quirino.brizi@gmail.com)]
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
angular
  .module('core.authentication')
  .factory('AuthenticationInterceptor', ['$location', function($location) {
	  return {
		request: function(config) {
			console.log('AuthenticationInterceptor - intercepted request');
			config.headers['Authentication'] = 'Basic abcd';
			return config;
		},
		responseError: function(response) {
			if (response.status === 401 || response.status === 403) {
	            console.log('unauthorized');
	            $location.path('/login');
	            return false;
	        } else if(response.status >= 400) {
	        	console.log('generic error');
	        	$location.path('/error');
	        }
			return response;
		}
	  };
  }])
  .factory('Login', ['$resource',
    function($resource) {
      return $resource('/authorize');
  	}
  ]);