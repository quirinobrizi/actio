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
	controller : [ 'Models', 'Modeler', '$routeParams', function EditorController(Models, Modeler, $routeParams) {
		var self = this,
			modelKey = $routeParams.modelKey;

		var modeler = Modeler.newInstance('#canvas','#properties');

		self.downloadBpmn = function(event) {
			modeler.saveXML({ format: true }, function(err, xml) {
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
							alert("model " + newModel.key + " published");
						}, function(error) {
							alert("unable to publish model " + newModel.key);
						});
					});
				}
			});
		};

		var openDiagram = function(diagram) {
			modeler.importXML(diagram, function(err) {
			    if (err) { console.error(err); } else { console.log('loaded.....'); }
			});
		};
		
		if(modelKey) {
			Models.get({key:modelKey}).$promise.then(function(model) {
					self.modelId = model.id; openDiagram(model.definition); 
			}, function(err) {
				alert("unable to open model " + modelKey); openDiagram(Modeler.initialDiagram());
			});
		} else {
			openDiagram(Modeler.initialDiagram());
		}
	} ]
});