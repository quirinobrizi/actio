/*******************************************************************************
 * Copyright [2016] [Quirino Brizi (quirino.brizi@gmail.com)]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
angular.module('editor').component('editor', {
	templateUrl : 'js/editor/template.html',
	controller : [ 'Models', function EditorController(Models) {
		var self = this;

		var modeler = new BpmnJS({
			container : '#canvas',
			propertiesPanel: {
			    parent: '#properties'
			  }
		});

		self.downloadBpmn = function(event) {
			extractXml(function(err, xml) {
				var blob = new Blob([xml], { type:"application/bpmn20-xml;charset=utf-8;" });			
				var downloadLink = angular.element('<a></a>');
                downloadLink.attr('href',window.URL.createObjectURL(blob));
                downloadLink.attr('download', 'diagram.bpmn20.xml');
				downloadLink[0].click();
			});
		};
		
		self.saveModel = function() {
			extractXml(function(err, xml) {
				var process = modeler.definitions.rootElements[0];
				var model = {'key': process.id, 'name': process.name, 'descriptor': xml};
				Models.save(model);
			});
		};
		
		var extractXml = function(callback) {
			modeler.saveXML({ format: true }, callback);
		}

		var newDiagram = function() {
			modeler.createDiagram(function(err) {
			    if (err) {
			      console.error(err);
			    } else {
			      console.log('loaded.....');
			    }
			});
		};
		
		newDiagram();
	} ]
});