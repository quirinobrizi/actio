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
package org.actio.modeler.app;

import java.util.Collection;

import org.actio.commons.message.bpmn.BpmnMessage;
import org.actio.modeler.domain.repository.BpmnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author quirino.brizi
 *
 */
@Service
public class BpmnService {

    @Autowired
    private BpmnRepository bpmnRepository;

    public Collection<BpmnMessage> getAllBpmn() {
        return bpmnRepository.findAll();
    }

}
