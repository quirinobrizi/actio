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

import java.util.Collection;
import java.util.List;

import org.actio.commons.message.bpmn.BpmnMessage;
import org.actio.engine.app.BpmnService;
import org.actio.engine.domain.model.bpmn.Bpmn;
import org.actio.engine.domain.model.bpmn.BpmnId;
import org.actio.engine.interfaces.translator.BpmnTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author quirino.brizi
 *
 */
@RestController
@RequestMapping(path = "/bpmns", produces = MediaType.APPLICATION_JSON_VALUE)
public class BpmnInterface {

    @Autowired
    private BpmnService bpmnService;
    @Autowired
    private BpmnTranslator bpmnTranslator;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<BpmnMessage> getAllBpmns() {
        List<Bpmn> bpmns = bpmnService.getAllBpmns();
        return bpmnTranslator.translate(bpmns);
    }

    @RequestMapping(path = "/{bpmnId}", method = RequestMethod.DELETE)
    public void deleteBpmn(@PathVariable(name = "bpmnId") String bpmnId) {
        bpmnService.deleteBpmn(BpmnId.newInstance(bpmnId));
    }
}
