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
		var self = this,
			initialDiagram = '<?xml version="1.0" encoding="UTF-8"?>' +
		  '<bpmn:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ' +
          	'xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL" ' +
          	'xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" ' +
          	'xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" ' +
          	'targetNamespace="http://bpmn.io/schema/bpmn" ' +
          	'id="Definitions_1">' +
          	  '<bpmn:process id="process_id">' +
			    '<bpmn:startEvent id="StartEvent_1"/>' +
			  '</bpmn:process>' +
			  '<bpmndi:BPMNDiagram id="BPMNDiagram_1">' +
			    '<bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_1">' +
			      '<bpmndi:BPMNShape id="_BPMNShape_StartEvent_2" bpmnElement="StartEvent_1">' +
			        '<dc:Bounds height="36.0" width="36.0" x="173.0" y="102.0"/>' +
			      '</bpmndi:BPMNShape>' +
			    '</bpmndi:BPMNPlane>' +
			  '</bpmndi:BPMNDiagram>' +
		  '</bpmn:definitions>';

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
		
		self.publishModel = function() {
			modeler.saveXML({ format: true }, function(err, xml) {
				if(err) {
					alert('unable to export model to XML' + err);
				} else {
					var process = modeler.definitions.rootElements[0],
						model = {'id': self.modelId, 'key': process.id, 'name': process.name, 'definition': xml};
					modeler.saveSVG(function(err, svg) {
						if(!err) {
							model['svg'] = svg;
						}
						Models.save(model).$promise.then(function(newModel) { 
							self.modelId = newModel.id; 
						});
					});
				}
			});
		};

		var newDiagram = function() {
			modeler.importXML(initialDiagram, function(err) {
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