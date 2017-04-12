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
  .module('models')
  .component('models', {
    templateUrl: 'js/models/template.html',
    controller: ['Bpmns', 'Processes', '$location', 'growl', function DashboardController(Bpmns, Processes, $location, growl) {
    	var self = this;
    	self.bpmns = Bpmns.query();
    	
    	self.launchBpmn = function(key) {
    		Processes.start({processId: key, action: "start"}).$promise.then(function(resp){ growl.info("process " + key + " started"); });
    	};
    	self.deleteBpmn = function(key) {
    		Bpmns.remove({key: key}).$promise.then(function(resp){ self.bpmns = resp; growl.info("process " + key + " deleted"); });
    	};
    	self.editBpmn = function(key) { $location.url('/editor/' + key); };
    	
    	self.latestModel = function(bpmn) {
    		var latestVersion = {versionId: -1};
    		for(v in bpmn.versions) {
    			var version = bpmn.versions[v];
    			if(version.versionId > latestVersion.versionId) {
    				latestVersion = version;
    			}
    		}
    		return latestVersion.model;
    	};
    }]});