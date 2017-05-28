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
package org.actio.engine.infrastructure.repository;

import java.util.Collection;
import java.util.List;

import org.actio.commons.message.NotFoundException;
import org.actio.engine.domain.model.bpmn.Bpmn;
import org.actio.engine.domain.model.bpmn.BpmnId;
import org.actio.engine.domain.model.bpmn.Error;
import org.actio.engine.domain.repository.BpmnRepository;
import org.actio.engine.infrastructure.repository.jpa.ErrorEventRepository;
import org.actio.engine.infrastructure.repository.storable.ErrorEventStorable;
import org.actio.engine.infrastructure.repository.translator.ErrorEventStorableTranslator;
import org.actio.engine.infrastructure.repository.translator.ProcessDefinitionTranslator;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author quirino.brizi
 *
 */
@Repository
public class BpmnRepositoryImpl implements BpmnRepository {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ErrorEventRepository errorEventRepository;
    @Autowired
    private ErrorEventStorableTranslator errorEventStorableTranslator;

    @Autowired
    private ProcessDefinitionTranslator processDefinitionTranslator;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.actio.engine.domain.repository.BpmnRepository#get(org.actio.engine.
     * domain.model.bpmn.BpmnId)
     */
    @Override
    public Bpmn get(BpmnId bpmnId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(bpmnId.toString())
                .latestVersion().singleResult();
        if (null == processDefinition) {
            throw NotFoundException.newInstance("unable to find BPMN identified by %s", bpmnId);
        }
        Bpmn bpmn = processDefinitionTranslator.translate(processDefinition);
        bpmn.setErrors(getErrors(bpmn.getId()));
        return bpmn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.actio.engine.domain.repository.BpmnRepository#getAll()
     */
    @Override
    public List<Bpmn> getAll() {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionKey().asc()
                .list();
        List<Bpmn> bpmns = processDefinitionTranslator.translate(processDefinitions);
        for (Bpmn bpmn : bpmns) {
            bpmn.setErrors(getErrors(bpmn.getId()));
        }
        return bpmns;
    }

    @Override
    public void remove(BpmnId bpmnId) {
        List<Deployment> deployments = repositoryService.createDeploymentQuery().processDefinitionKey(bpmnId.toString()).list();
        if (null != deployments) {
            for (Deployment deployment : deployments) {
                repositoryService.deleteDeployment(deployment.getId(), true);
            }
        }
        List<Model> models = repositoryService.createModelQuery().modelKey(bpmnId.toString()).list();
        if (null != models) {
            for (Model model : models) {
                repositoryService.deleteModel(model.getId());
            }
        }
    }

    private Collection<Error> getErrors(String processDefinitionKey) {
        List<ErrorEventStorable> storables = errorEventRepository.findAllByProcessDefinitionKey(processDefinitionKey);
        return errorEventStorableTranslator.translate(storables);
    }
}
