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
		var authentication = getItem('authentication');
		return authentication != null && authentication != undefined;
	  };
	  
	  this.clear = function() {
		var authentication = localStorageService.remove('authentication');
		Bus.emit('actio.authentication.cleared', authentication);
	  };
	  
	  this.set = function(authentication) {
		localStorageService.set('authentication', authentication);
		Bus.emit('actio.authentication.recorded', authentication);
	  };
	  
	  
	  this.details = function() {
		  return getItem('authentication');
	  };
	  
	  var getItem = function(key) {
		  var authentication = localStorageService.get('authentication');
		  if(null == authentication) {
			  return null;
		  }
		  console.log(authentication.expiryTime - new Date().getTime());
		  return authentication != null && authentication.expiryTime > new Date().getTime() ? authentication : null;
	  };
	  
  }]);