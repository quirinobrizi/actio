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
package org.actio.engine.interfaces;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.actio.commons.message.NotFoundException;
import org.actio.commons.message.process.DeployProcessRequestMessage;
import org.actio.commons.message.process.ProcessMessage;
import org.actio.commons.message.process.UpdateProcessStateRequestMessage;
import org.actio.engine.app.ProcessService;
import org.actio.engine.domain.model.bpmn.Action;
import org.actio.engine.domain.model.bpmn.Bpmn;
import org.actio.engine.domain.model.bpmn.Inputs;
import org.actio.engine.domain.model.bpmn.process.ProcessId;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Defines the contract for actions over processes. Is responsible for receiving
 * HTTP requests and validate the content of the request.
 * 
 * @author quirino.brizi
 *
 */
@RestController
@RequestMapping(path = "/processes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProcessInterface {

    @Autowired
    private ProcessService processService;

    @Autowired
    private RepositoryService repositoryService;

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ProcessMessage deploy(@RequestBody DeployProcessRequestMessage deployProcessRequestMessage) {
        String modelId = deployProcessRequestMessage.getModelId();
        Model model = repositoryService.getModel(modelId);
        if (null != model) {
            String name = String.format("%s_%s.bpmn20.xml", model.getKey(), modelId);
            byte[] source = repositoryService.getModelEditorSource(modelId);
            Deployment deployment = repositoryService.createDeployment().name(name).addInputStream(name, new ByteArrayInputStream(source))
                    .tenantId(model.getTenantId()).category(model.getCategory()).deploy();
            return new ProcessMessage(deployment.getId(), deployment.getName(), null);
        }
        throw NotFoundException.newInstance("cannot deploy requested model");
    }

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
    public ProcessMessage updateState(@RequestBody UpdateProcessStateRequestMessage updateProcessStatusRequestMessage) {
        String alias = updateProcessStatusRequestMessage.getAction();
        String processId = updateProcessStatusRequestMessage.getProcessId();
        Map<String, Object> inputs = updateProcessStatusRequestMessage.getInputs();
        Bpmn process = processService.updateState(Action.get(alias), ProcessId.newInstance(processId), new Inputs(inputs));
        return new ProcessMessage(process.getId(), process.getName(), null);
    }

    @RequestMapping(path = "/{processKey}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(name = "processKey") String processKey) {
        List<Deployment> deployments = repositoryService.createDeploymentQuery().processDefinitionKey(processKey).list();
        if (null != deployments) {
            for (Deployment deployment : deployments) {
                List<Model> models = repositoryService.createModelQuery().modelKey(processKey).list();
                if (null != models) {
                    for (Model model : models) {
                        repositoryService.deleteDeployment(deployment.getId(), true);
                        repositoryService.deleteModel(model.getId());
                    }
                }
            }
        }
    }

}
