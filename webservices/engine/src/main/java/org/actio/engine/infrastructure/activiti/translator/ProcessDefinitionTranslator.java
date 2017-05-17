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
package org.actio.engine.infrastructure.activiti.translator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.actio.engine.domain.model.bpmn.Bpmn;
import org.actio.engine.domain.model.bpmn.BpmnId;
import org.actio.engine.domain.model.bpmn.Version;
import org.actio.engine.domain.model.bpmn.VersionId;
import org.actio.engine.domain.model.bpmn.model.Model;
import org.actio.engine.domain.model.bpmn.model.ModelId;
import org.actio.engine.domain.model.bpmn.model.Resource;
import org.actio.engine.domain.model.bpmn.model.ResourceType;
import org.actio.engine.domain.model.bpmn.process.Instance;
import org.actio.engine.domain.model.bpmn.process.InstanceId;
import org.actio.engine.domain.model.bpmn.process.InstanceState;
import org.actio.engine.domain.model.bpmn.process.Process;
import org.actio.engine.domain.model.bpmn.process.ProcessId;
import org.actio.engine.infrastructure.Translator;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class ProcessDefinitionTranslator implements Translator<Bpmn, ProcessDefinition> {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

    @Autowired
    private ExecutionEntityTranslator executionEntityTranslator;

    @Override
    public List<Bpmn> translate(Collection<ProcessDefinition> processDefinitions) {
        Map<BpmnId, Bpmn> bpmns = new HashMap<>();
        for (ProcessDefinition processDefinition : processDefinitions) {
            BpmnId bpmnId = BpmnId.newInstance(processDefinition.getKey());
            Bpmn bpmn;
            if (bpmns.containsKey(bpmnId)) {
                bpmn = bpmns.get(bpmnId);
            } else {
                bpmn = new Bpmn(bpmnId, processDefinition.getName());
                bpmns.put(bpmnId, bpmn);
            }
            bpmn.addVersion(extractVersion(processDefinition, bpmnId, bpmn));
        }
        return new ArrayList<>(bpmns.values());
    }

    @Override
    public Bpmn translate(ProcessDefinition processDefinition) {
        BpmnId bpmnId = BpmnId.newInstance(processDefinition.getKey());
        Bpmn bpmn = new Bpmn(bpmnId, processDefinition.getName());
        bpmn.addVersion(extractVersion(processDefinition, bpmnId, bpmn));
        return bpmn;
    }

    private Version extractVersion(ProcessDefinition processDefinition, BpmnId bpmnId, Bpmn bpmn) {
        VersionId versionId = VersionId.newInstance(processDefinition.getVersion());
        Version version;
        String processDefinitionId = processDefinition.getId();
        if (bpmn.hasVersion(versionId)) {
            version = bpmn.getVersion(versionId);
        } else {
            version = new Version(versionId, Process.newInstance(ProcessId.newInstance(processDefinitionId)));
            Model model = extractModel(bpmnId);
            version.setModel(model);
        }
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().includeProcessVariables()
                .processDefinitionId(processDefinitionId).list();
        for (ProcessInstance processInstance : processInstances) {
            ExecutionEntity entity = (ExecutionEntity) processInstance;
            Instance instance = executionEntityTranslator.translate(entity);
            version.addProcessInstance(instance);
        }
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .includeProcessVariables().finished().processDefinitionId(processDefinitionId).list();
        for (HistoricProcessInstance processInstance : historicProcessInstances) {
            Instance instance = new Instance(InstanceId.newInstance(processInstance.getId()));
            instance.setVariables(processInstance.getProcessVariables());
            instance.setInstanceState(InstanceState.TERMINATED);
            instance.setStartDate(processInstance.getStartTime());
            instance.setEndDate(processInstance.getEndTime());
            version.addProcessInstance(instance);
        }
        return version;
    }

    private Model extractModel(BpmnId bpmnId) {
        List<org.activiti.engine.repository.Model> storedModels = repositoryService.createModelQuery().modelKey(bpmnId.toString())
                .orderByModelVersion().desc().list();
        if (null != storedModels && !storedModels.isEmpty()) {
            org.activiti.engine.repository.Model storedModel = storedModels.get(0);
            Model model = new Model(ModelId.newInstance(storedModel.getId()));
            if (storedModel.hasEditorSource()) {
                model.addResource(ResourceType.XML,
                        Resource.newInstance(new String(repositoryService.getModelEditorSource(storedModel.getId()))));
            }
            if (storedModel.hasEditorSourceExtra()) {
                model.addResource(ResourceType.SVG,
                        Resource.newInstance(new String(repositoryService.getModelEditorSourceExtra(storedModel.getId()))));
            }
            return model;
        }
        return null;
    }
}
