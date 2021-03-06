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
package org.actio.engine.app;

import java.util.List;

import org.actio.engine.domain.model.bpmn.Bpmn;
import org.actio.engine.domain.model.bpmn.BpmnId;
import org.actio.engine.domain.model.bpmn.Inputs;

/**
 * @author quirino.brizi
 *
 */
public interface BpmnService {

    List<Bpmn> getAllBpmns();

    void deleteBpmn(BpmnId newInstance);

    /**
     * Start a new BPMN processes instance.
     * 
     * @param bpmnId
     *            the BPMN identifier
     * @param inputs
     *            the BPMN process instance inputs
     * @return
     */
    Bpmn startNewProcessInstance(BpmnId bpmnId, Inputs inputs);

}
