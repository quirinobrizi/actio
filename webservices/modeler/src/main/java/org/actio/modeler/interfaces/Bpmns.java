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
package org.actio.modeler.interfaces;

import java.util.Collection;

import org.actio.commons.message.bpmn.BpmnMessage;
import org.actio.commons.message.bpmn.InputMessage;
import org.actio.modeler.app.BpmnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Allows to retrieve BPMNs information.
 * 
 * @author quirino.brizi
 *
 */
@RestController
@RequestMapping(path = "/bpmns", produces = MediaType.APPLICATION_JSON_VALUE)
public class Bpmns {

    @Autowired
    private BpmnService bpmnService;

    @ResponseStatus(code = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET)
    public Collection<BpmnMessage> getAll() {
        return bpmnService.getAllBpmn();
    }

    @RequestMapping(path = "/{bpmnId}", method = RequestMethod.DELETE)
    public Collection<BpmnMessage> deleteBpmn(@PathVariable(name = "bpmnId") String bpmnId) {
        return bpmnService.deleteBpmn(bpmnId);
    }

    @RequestMapping(path = "/{bpmnId}/start", method = RequestMethod.POST)
    public Collection<BpmnMessage> startBpmnInstance(@PathVariable(name = "bpmnId") String bpmnId, @RequestBody InputMessage inputMessage) {
        return bpmnService.startBpmnInstance(bpmnId, inputMessage);
    }
}
