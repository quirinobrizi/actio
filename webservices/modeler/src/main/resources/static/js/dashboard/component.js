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
  .module('dashboard')
  .component('dashboard', {
    templateUrl: 'js/dashboard/template.html',
    controller: ['Bpmns',  function DashboardController(Bpmns) {
    	var self = this;
    	Bpmns.query(function(bpmns) {
    		self.statistics = calculateStatistics(bpmns);
    	});
    	
    	var calculateStatistics = function(bpmns) {
    		return {
    			bpmns: bpmns.length,
    			processes: countProcesses(bpmns),
    			instances: countInstances(bpmns),
    			terminatedInstances: countInstances(bpmns, 'TERMINATED'),
    			activeInstances: countInstances(bpmns, 'ACTIVE'),
    			suspendedInstances: countInstances(bpmns, 'SUSPENDED')
    		};
    	};
    	
    	var countProcesses = function(bpmns) {
    		var answer = 0;
    		for(var b in bpmns) {
    			var bpmn = bpmns[b];
	    		for(var key in bpmn.versions) {
	    			var version = bpmn.versions[key];
	    			answer += version.processes.length;
	    		}
    		}
    		return answer;
    	};
    	
    	var countInstances = function(bpmns, instanceState) {
    		var answer = 0;
    		for(var b in bpmns) {
    			var bpmn = bpmns[b];
	    		for(var v in bpmn.versions) {
	    			var version = bpmn.versions[v];
	    			for(var p in version.processes) {
	    				if(!instanceState) {
	    					answer += version.processes[p].instances.length;
	    				} else {
	    					for(var i in version.processes[p].instances) {
	    						var instance = version.processes[p].instances[i];
	    						if(instance.instanceState === instanceState) {
	    							answer += 1;
	    						}
	    					}
	    				}
	    			}
	    		}
    		}
    		return answer;
    	};
    }]});