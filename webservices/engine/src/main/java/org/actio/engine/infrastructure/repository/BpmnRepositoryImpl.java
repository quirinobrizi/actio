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

import java.util.List;

import org.actio.engine.domain.model.bpmn.Bpmn;
import org.actio.engine.domain.repository.BpmnRepository;
import org.actio.engine.infrastructure.repository.translator.ProcessDefinitionTranslator;
import org.activiti.engine.RepositoryService;
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
    private ProcessDefinitionTranslator processDefinitionTranslator;

    /*
     * (non-Javadoc)
     * 
     * @see org.actio.engine.domain.repository.BpmnRepository#getAll()
     */
    @Override
    public List<Bpmn> getAll() {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionKey().asc()
                .list();
        return processDefinitionTranslator.translate(processDefinitions);
    }

}
