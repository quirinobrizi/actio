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
    controller: ['Bpmns', '$uibModal', '$location', 'growl', function DashboardController(Bpmns, $uibModal, $location, growl) {
    	var self = this;
    	self.bpmns = Bpmns.query();
    	
    	self.launchBpmn = function(key) {
    		Bpmns.startBpmnProcessInstance({key: key}).$promise.then(function(bpmns){ 
    			growl.info("process " + key + " started"); 
    			self.bpmns = bpmns;
    		});
    	};
    	self.deleteBpmn = function(key) {
    		Bpmns.remove({key: key}).$promise.then(function(resp){ 
    			self.bpmns = resp; 
    			growl.info("process " + key + " deleted"); 
    		});
    	};
    	self.editBpmn = function(key) { $location.url('/editor/' + key); };
    	
    	self.info = function(version) {
    		$uibModal.open({
				animation: true,
				ariaLabelledBy: 'modal-title',
				ariaDescribedBy: 'modal-body',
				templateUrl: 'js/models/infoModalTemplate.html',
				controller: function ($uibModalInstance, version) {
				    var $ctrl = this;
				    $ctrl.version = version;

				    $ctrl.close = function () {
				      $uibModalInstance.close();
				    };
				},
				controllerAs: '$ctrl',
				size: 'lg',
				appendTo: null,
				resolve: {
					version: function () {
						return version;
					}
				}
			});
    	};
    	
    	self.latestModel = function(bpmn) {
    		var latestVersion = {versionId: -1};
    		for(var v in bpmn.versions) {
    			var version = bpmn.versions[v];
    			if(version.versionId > latestVersion.versionId) {
    				latestVersion = version;
    			}
    		}
    		return latestVersion.model;
    	};
    	
    	self.countInstancesState = function(process, state) {
    		var count = 0;
    		for(var i in process.instances) {
    			var inst = process.instances[i];
    			if(state === inst.instanceState) {
    				count++;
    			}
			}
    		return count;
    	};
    	
    	self.showErrors = function(bpmn) {
			$uibModal.open({
				animation: true,
				ariaLabelledBy: 'modal-title',
				ariaDescribedBy: 'modal-body',
				templateUrl: 'js/models/errorModalTemplate.html',
				controller: function ($uibModalInstance, bpmn) {
				    var $ctrl = this;
				    $ctrl.bpmn = bpmn;

				    $ctrl.close = function () {
				      $uibModalInstance.close();
				    };
				},
				controllerAs: '$ctrl',
				size: 'lg',
				appendTo: null,
				resolve: {
					bpmn: function () {
						return bpmn;
					}
				}
			});
		};
  }]});