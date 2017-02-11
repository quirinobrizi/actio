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
package org.actio.modeler.interfaces;

import org.actio.modeler.app.ModelService;
import org.actio.modeler.domain.model.Model;
import org.actio.modeler.infrastructure.repository.message.response.ModelResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author quirino.brizi
 *
 */
@RestController
@RequestMapping(path = "/models", produces = MediaType.APPLICATION_JSON_VALUE)
public class Models {

	@Autowired
	private ModelService modelService;

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ModelResponseMessage saveModel(@RequestBody Model model) {
		return modelService.create(model);
	}
}
