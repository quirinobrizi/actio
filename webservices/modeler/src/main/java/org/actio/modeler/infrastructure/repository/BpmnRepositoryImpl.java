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

import java.util.Collection;
import java.util.List;

import org.actio.commons.message.bpmn.BpmnMessage;
import org.actio.commons.message.bpmn.InputMessage;
import org.actio.modeler.domain.repository.BpmnRepository;
import org.actio.modeler.infrastructure.config.ModelerConfigurationProperties;
import org.actio.modeler.infrastructure.http.ClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author quirino.brizi
 *
 */
@Repository
public class BpmnRepositoryImpl implements BpmnRepository {

    @Autowired
    private ClientFactory clientFactory;
    @Autowired
    private ModelerConfigurationProperties configuration;

    /*
     * (non-Javadoc)
     * 
     * @see org.actio.modeler.domain.repository.BpmnRepository#findAll()
     */
    @Override
    public Collection<BpmnMessage> findAll() {
        String urlFormat = configuration.getEngine().getUrlFormat();
        ParameterizedTypeReference<List<BpmnMessage>> responseType = new ParameterizedTypeReference<List<BpmnMessage>>() {
        };
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlFormat);
        RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.GET, builder.buildAndExpand("bpmns").toUri());
        return clientFactory.newClient().exchange(requestEntity, responseType).getBody();
    }

    @Override
    public void remove(String bpmnId) {
        String urlFormat = configuration.getEngine().getUrlFormat();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlFormat);
        RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.DELETE,
                builder.pathSegment(bpmnId).buildAndExpand("bpmns").toUri());
        clientFactory.newClient().exchange(requestEntity, Void.class);
    }

    @Override
    public void startBpmnInstance(String bpmnId, InputMessage inputMessage) {
        String urlFormat = configuration.getEngine().getUrlFormat();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlFormat);
        RequestEntity<InputMessage> requestEntity = new RequestEntity<>(inputMessage, HttpMethod.POST,
                builder.pathSegment(bpmnId, "start").buildAndExpand("bpmns").toUri());
        clientFactory.newClient().exchange(requestEntity, Void.class);
    }

}
