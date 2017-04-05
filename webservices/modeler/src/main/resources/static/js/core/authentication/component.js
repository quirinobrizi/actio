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
  .component('authentication', {
    templateUrl: 'js/core/authentication/template.html',
    controller: ['Login', function DashboardController(Login) {
    	var self = this;
    	self.login = function(credential) {
    		console.log('do login', credential);
    		Login.save(null, credential, success, error);
    	};
    	var success = function(resp, headers, statusCode, statusText) {
    		console.log('success', resp, headers, statusCode, statusText);
		};
		
		var error = function(resp) {
			console.log('error', resp);
		};
    }]});