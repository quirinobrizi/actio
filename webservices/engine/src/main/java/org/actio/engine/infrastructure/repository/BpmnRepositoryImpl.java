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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.actio.engine.domain.model.bpmn.Bpmn;
import org.actio.engine.domain.model.bpmn.BpmnId;
import org.actio.engine.domain.model.bpmn.Version;
import org.actio.engine.domain.model.bpmn.VersionId;
import org.actio.engine.domain.model.bpmn.model.Model;
import org.actio.engine.domain.model.bpmn.model.Resource;
import org.actio.engine.domain.model.bpmn.model.ResourceType;
import org.actio.engine.domain.model.bpmn.process.Process;
import org.actio.engine.domain.model.bpmn.process.ProcessId;
import org.actio.engine.domain.model.bpmn.process.ProcessState;
import org.actio.engine.domain.repository.BpmnRepository;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author quirino.brizi
 *
 */
@Repository
public class BpmnRepositoryImpl implements BpmnRepository {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;

    /*
     * (non-Javadoc)
     * 
     * @see org.actio.engine.domain.repository.BpmnRepository#getAll()
     */
    @Override
    public List<Bpmn> getAll() {
        Map<BpmnId, Bpmn> bpmns = new HashMap<>();
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionKey().asc()
                .list();
        for (ProcessDefinition processDefinition : processDefinitions) {
            BpmnId bpmnId = BpmnId.newInstance(processDefinition.getKey());
            Bpmn bpmn = bpmns.containsKey(bpmnId) ? bpmns.get(bpmnId) : new Bpmn(bpmnId, processDefinition.getName());
            bpmn.addVersion(extractVersion(processDefinition, bpmnId, bpmn));
        }
        return new ArrayList<>(bpmns.values());
    }

    private Version extractVersion(ProcessDefinition processDefinition, BpmnId bpmnId, Bpmn bpmn) {
        VersionId versionId = VersionId.newInstance(processDefinition.getVersion());
        Version version;
        if (bpmn.hasVersion(versionId)) {
            version = bpmn.getVersion(versionId);
        } else {
            version = new Version(versionId);
            Model model = new Model();
            org.activiti.engine.repository.Model storedModel = repositoryService.createModelQuery().modelKey(bpmnId.toString())
                    .singleResult();
            if (storedModel.hasEditorSource()) {
                model.addResource(ResourceType.XML,
                        Resource.newInstance(new String(repositoryService.getModelEditorSource(storedModel.getId()))));
            }
            if (storedModel.hasEditorSourceExtra()) {
                model.addResource(ResourceType.SVG,
                        Resource.newInstance(new String(repositoryService.getModelEditorSourceExtra(storedModel.getId()))));
            }
            version.setModel(model);
        }
        String processDefinitionId = processDefinition.getId();
        Process process = Process.newInstance(ProcessId.newInstance(processDefinitionId));
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().includeProcessVariables()
                .processDefinitionId(processDefinitionId).singleResult();
        process.setVariables(processInstance.getProcessVariables());
        ProcessState processState;
        if (processInstance.isEnded()) {
            processState = ProcessState.TERMINATED;
        } else if (processInstance.isSuspended()) {
            processState = ProcessState.SUSPENDED;
        } else {
            processState = ProcessState.ACTIVE;
        }
        process.setProcessState(processState);
        process.setInstanceId(processInstance.getProcessInstanceId());
        version.addProcess(process);
        return version;
    }

}
