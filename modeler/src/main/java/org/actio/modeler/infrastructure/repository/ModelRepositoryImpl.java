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
package org.actio.modeler.infrastructure.repository;

import org.actio.modeler.domain.model.Model;
import org.actio.modeler.domain.repository.ModelRepository;
import org.actio.modeler.infrastructure.config.ModelerConfigurationProperties;
import org.actio.modeler.infrastructure.repository.message.ModelMessage;
import org.actio.modeler.infrastructure.repository.message.response.ModelResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

/**
 * @author quirino.brizi
 *
 */
@Repository
public class ModelRepositoryImpl implements ModelRepository {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ModelerConfigurationProperties configuration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.actio.modeler.domain.repository.ModelRepository#add(org.actio.modeler
	 * .domain.model.Model)
	 */
	@Override
	public ModelResponseMessage add(Model model) {
		ModelMessage message = new ModelMessage(model.getName(), model.getKey(), model.getCategory(),
				model.getVersion(), model.getMetaInfo(), model.getDeploymentId(), model.getTenantId());
		return restTemplate.postForObject(configuration.getEngine().getUrlFormat(), message, ModelResponseMessage.class,
				"repository/models");
	}

}
