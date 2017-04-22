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
package org.actio.engine.infrastructure.repository.translator;

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
    private JobEntityTranslator jobEntityTranslator;
    @Autowired
    private TaskEntityTranslator taskEntityTranslator;

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

    private Version extractVersion(ProcessDefinition processDefinition, BpmnId bpmnId, Bpmn bpmn) {
        VersionId versionId = VersionId.newInstance(processDefinition.getVersion());
        Version version;
        if (bpmn.hasVersion(versionId)) {
            version = bpmn.getVersion(versionId);
        } else {
            version = new Version(versionId);
            Model model = extractModel(bpmnId);
            version.setModel(model);
        }
        String processDefinitionId = processDefinition.getId();
        Process process = Process.newInstance(ProcessId.newInstance(processDefinitionId));
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().includeProcessVariables()
                .processDefinitionId(processDefinitionId).list();
        for (ProcessInstance processInstance : processInstances) {
            ExecutionEntity entity = (ExecutionEntity) processInstance;
            Instance instance = new Instance(InstanceId.newInstance(entity.getId()));
            instance.setVariables(entity.getProcessVariables());
            InstanceState instanceState;
            if (entity.isEnded()) {
                instanceState = InstanceState.TERMINATED;
            } else if (entity.isSuspended()) {
                instanceState = InstanceState.SUSPENDED;
            } else {
                instanceState = InstanceState.ACTIVE;
            }
            instance.setInstanceState(instanceState);
            instance.setStartDate(entity.getLockTime());
            instance.setJobs(jobEntityTranslator.translate(entity.getJobs()));
            instance.setTasks(taskEntityTranslator.translate(entity.getTasks()));
            process.addInstance(instance);
        }
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .includeProcessVariables().finished().processDefinitionId(processDefinition.getId()).list();
        for (HistoricProcessInstance processInstance : historicProcessInstances) {
            Instance instance = new Instance(InstanceId.newInstance(processInstance.getId()));
            instance.setVariables(processInstance.getProcessVariables());
            instance.setInstanceState(InstanceState.TERMINATED);
            instance.setStartDate(processInstance.getStartTime());
            instance.setEndDate(processInstance.getEndTime());
            process.addInstance(instance);
        }
        version.addProcess(process);
        return version;
    }

    private Model extractModel(BpmnId bpmnId) {
        org.activiti.engine.repository.Model storedModel = repositoryService.createModelQuery().modelKey(bpmnId.toString()).singleResult();
        if (null != storedModel) {
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

    @Override
    public Bpmn translate(ProcessDefinition input) {
        return null;
    }
}
