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

import java.util.List;

import org.actio.commons.message.model.ModelMessage;
import org.actio.commons.message.process.DeployProcessRequestMessage;
import org.actio.commons.message.process.ProcessMessage;
import org.actio.modeler.domain.repository.ModelRepository;
import org.actio.modeler.infrastructure.config.ModelerConfigurationProperties;
import org.actio.modeler.infrastructure.http.ClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Allow access models from Action engine.
 * 
 * @author quirino.brizi
 *
 */
@Repository
public class ModelRepositoryImpl implements ModelRepository {

    @Autowired
    private ClientFactory clientFactory;
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
    public ModelMessage add(ModelMessage model) {
        String urlFormat = configuration.getEngine().getUrlFormat();
        ModelMessage modelMessage = clientFactory.newClient().postForObject(urlFormat, model, ModelMessage.class, "models");
        DeployProcessRequestMessage request = new DeployProcessRequestMessage(modelMessage.getId());
        clientFactory.newClient().postForObject(urlFormat, request, ProcessMessage.class, "processes");
        return modelMessage;
    }

    @Override
    public List<ModelMessage> getAllModels() {
        String urlFormat = configuration.getEngine().getUrlFormat();
        ParameterizedTypeReference<List<ModelMessage>> responseType = new ParameterizedTypeReference<List<ModelMessage>>() {
        };
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlFormat);
        RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.GET, builder.buildAndExpand("models").toUri());
        return clientFactory.newClient().exchange(requestEntity, responseType).getBody();
    }

    @Override
    public ModelMessage getModel(String modelKey) {
        String urlFormat = configuration.getEngine().getUrlFormat();
        return clientFactory.newClient().getForObject(urlFormat, ModelMessage.class, String.format("models/%s", modelKey));
    }
}
