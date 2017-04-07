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
  .factory('Login', ['$resource',
    function($resource) {
      return $resource('/authorize');
  	}
  ])
  .service('Auth', ['Bus', 'localStorageService', function(Bus, localStorageService) {
	  this.isAuthenticated = function() {
		var user = localStorageService.get('authenticated.user');
		return user != null && user != undefined;
	  };
	  
	  this.clear = function() {
		var user = localStorageService.remove('authenticated.user');
		Bus.emit('actio.authenticated.user.cleared', user);
	  };
	  
	  this.set = function(user) {
		localStorageService.set('authenticated.user', user);
		Bus.emit('actio.authenticated.user.recorded', user);
	  };
	  
	  
	  this.details = function() {
		  return localStorageService.get('authenticated.user');
	  };
	  
  }]);