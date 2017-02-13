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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.actio.commons.message.process.DeployProcessRequestMessage;
import org.actio.commons.message.process.ProcessMessage;
import org.actio.commons.message.process.UpdateProcessStateRequestMessage;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
	private RuntimeService runtimeService;
	@Autowired
	private RepositoryService repositoryService;

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ProcessMessage deploy(@RequestBody DeployProcessRequestMessage deployProcessRequestMessage) {
		String modelId = deployProcessRequestMessage.getModelId();
		Model model = repositoryService.getModel(modelId);
		if (null != model) {
			String name = String.format("%s.bpmn20.xml", model.getKey());
			byte[] source = repositoryService.getModelEditorSource(modelId);
			Deployment deployment = repositoryService.createDeployment().name(name)
					.addInputStream(name, new ByteArrayInputStream(source)).tenantId(model.getTenantId())
					.category(model.getCategory()).deploy();
			return new ProcessMessage(deployment.getId(), deployment.getName(), null);
		}
		// TODO: QB define custom exception...
		throw new RuntimeException("cannot deploy requested model");
	}

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public ProcessMessage start(@RequestBody UpdateProcessStateRequestMessage updateProcessStatusRequestMessage) {
		String alias = updateProcessStatusRequestMessage.getAction();
		String processId = updateProcessStatusRequestMessage.getProcessId();
		Map<String, Object> inputs = updateProcessStatusRequestMessage.getInputs();
		ProcessInstance processInstance = Action.get(alias).execute(runtimeService, processId, inputs);
		return new ProcessMessage(processInstance.getId(), processInstance.getName(), null);
	}

	private static enum Action {
		START("start") {
			@Override
			public ProcessInstance execute(RuntimeService runtimeService, String processId,
					Map<String, Object> inputs) {
				return runtimeService.startProcessInstanceByKey(processId, inputs);
			};
		},
		STOP("stop") {
			@Override
			public ProcessInstance execute(RuntimeService runtimeService, String processId,
					Map<String, Object> inputs) {
				return null;
			};
		},
		RESUME("resume") {
			@Override
			public ProcessInstance execute(RuntimeService runtimeService, String processId,
					Map<String, Object> inputs) {
				return null;
			};
		};

		private final List<String> aliases;

		private Action(String... aliases) {
			this.aliases = Arrays.asList(aliases);
		}

		public abstract ProcessInstance execute(RuntimeService runtimeService, String processId,
				Map<String, Object> inputs);

		public static Action get(String alias) {
			for (Action action : values()) {
				if (action.aliases.contains(alias.toLowerCase())) {
					return action;
				}
			}
			throw new IllegalArgumentException();
		}
	}
}
