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
	controller : [ 'Processes', function EditorController(Processes) {
		var self = this;

		self.modeler = new BpmnJS({
			container : '#canvas',
			propertiesPanel: {
			    parent: '#properties'
			  }
		});

		var newDiagram = function() {
			var diagram = '<?xml version="1.0" encoding="UTF-8"?> <bpmn2:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:bpmn2="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:di="http://www.omg.org/spec/DD/20100524/DI" xsi:schemaLocation="http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd" id="sample-diagram" targetNamespace="http://bpmn.io/schema/bpmn"> <bpmn2:process id="Process_1" isExecutable="false"><bpmn2:startEvent id="StartEvent_1"/></bpmn2:process><bpmndi:BPMNDiagram id="BPMNDiagram_1"><bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_1"><bpmndi:BPMNShape id="_BPMNShape_StartEvent_2" bpmnElement="StartEvent_1"><dc:Bounds height="36.0" width="36.0" x="412.0" y="240.0"/></bpmndi:BPMNShape></bpmndi:BPMNPlane></bpmndi:BPMNDiagram></bpmn2:definitions>';
			self.modeler.importXML(diagram, function(err) {
			    if (err) {
//			      container
//			        .removeClass('with-diagram')
//			        .addClass('with-error');
//			      container.find('.error pre').text(err.message);
			      console.error(err);
			    } else {
//			      container
//			        .removeClass('hidden')
//			        .addClass('with-diagram');
			      console.log('loaded.....');
			    }
			});
		};
		
		newDiagram();
	} ]
});