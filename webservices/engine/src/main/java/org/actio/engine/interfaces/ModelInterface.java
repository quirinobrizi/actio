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

import java.util.ArrayList;
import java.util.List;

import org.actio.commons.message.NotFoundException;
import org.actio.commons.message.model.ModelMessage;
import org.actio.engine.interfaces.translator.ModelMessageTranslator;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author quirino.brizi
 *
 */
@RestController
@RequestMapping("/models")
public class ModelInterface {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ModelMessageTranslator modelMessageTranslator;

    @ResponseStatus(code = HttpStatus.OK)
    @RequestMapping(path = "/{modelKey}", method = RequestMethod.GET, produces = "application/json")
    public ModelMessage getModel(@PathVariable("modelKey") String modelKey) {
        Model model = repositoryService.createModelQuery().modelKey(modelKey).singleResult();
        if (null != model) {
            String id = model.getId();
            String definition = model.hasEditorSource() ? new String(repositoryService.getModelEditorSource(id)) : null;
            String svg = model.hasEditorSourceExtra() ? new String(repositoryService.getModelEditorSourceExtra(id)) : null;
            return modelMessageTranslator.translate(model, definition, svg);
        }
        throw NotFoundException.newInstance("unable to find model %s", modelKey);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<ModelMessage> getAllModels() {
        List<ModelMessage> answer = new ArrayList<>();
        List<Model> models = repositoryService.createModelQuery().list();
        for (Model model : models) {
            String id = model.getId();
            String definition = model.hasEditorSource() ? new String(repositoryService.getModelEditorSource(id)) : null;
            String svg = model.hasEditorSourceExtra() ? new String(repositoryService.getModelEditorSourceExtra(id)) : null;
            answer.add(modelMessageTranslator.translate(model, definition, svg));
        }
        return answer;
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ModelMessage createModel(@RequestBody ModelMessage modelMessage) {
        Model model = null != modelMessage.getId() ? repositoryService.getModel(modelMessage.getId()) : repositoryService.newModel();
        model.setCategory(modelMessage.getCategory());
        model.setDeploymentId(modelMessage.getDeploymentId());
        model.setKey(modelMessage.getKey());
        model.setMetaInfo(modelMessage.getMetaInfo());
        model.setName(modelMessage.getName());
        model.setVersion(modelMessage.getVersion());
        model.setTenantId(modelMessage.getTenantId());

        repositoryService.saveModel(model);
        String id = model.getId();
        if (null != modelMessage.getDefinition()) {
            repositoryService.addModelEditorSource(id, modelMessage.getDefinition().getBytes());
        }
        if (null != modelMessage.getSvg()) {
            repositoryService.addModelEditorSourceExtra(id, modelMessage.getSvg().getBytes());
        }
        return modelMessageTranslator.translate(model, modelMessage.getDefinition(), modelMessage.getSvg());
    }
}
