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
    			//processes: countProcesses(bpmns),
    			instances: countInstances(bpmns),
    			terminatedInstances: countInstances(bpmns, 'TERMINATED'),
    			activeInstances: countInstances(bpmns, 'ACTIVE'),
    			suspendedInstances: countInstances(bpmns, 'SUSPENDED'),
    			jobs: countJobs(bpmns),
    			tasks: countTasks(bpmns)
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
    		var answer = 0,
    			instances = getIntances(bpmns);
    		if(!instanceState) {
    			return instances.length;
    		}
			for(var i in instances) {
				var instance = instances[i];
				if(instance.instanceState === instanceState) {
					answer += 1;
				}
			}
    		return answer;
    	};
    	
    	var countJobs = function(bpmns) {
    		var answer = 0,
				instances = getIntances(bpmns);
    		for(var i in instances) {
				var instance = instances[i];
				answer += instances[i].jobs ? instances[i].jobs.length : 0;
    		}
    		return answer;
    	};
    	
    	var countTasks = function(bpmns) {
    		var answer = 0,
				instances = getIntances(bpmns);
    		for(var i in instances) {
				var instance = instances[i];
				answer += instances[i].tags ? instances[i].tags.length : 0;
    		}
    		return answer;
    	};
    	
    	var getIntances = function(bpmns) {
    		var answer = [];
    		for(var b in bpmns) {
    			var bpmn = bpmns[b];
	    		for(var v in bpmn.versions) {
	    			var version = bpmn.versions[v];
	    			for(var p in version.processes) {
    					for(var i in version.processes[p].instances) {
    						answer.push(version.processes[p].instances[i]);
    					}
	    			}
	    		}
    		}
    		return answer;
    	};
    }]});