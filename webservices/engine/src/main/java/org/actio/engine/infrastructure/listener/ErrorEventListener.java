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
package org.actio.engine.infrastructure.listener;

import org.actio.engine.infrastructure.repository.BpmnErrorEvent;
import org.actio.engine.infrastructure.repository.jpa.ErrorEventRepository;
import org.actio.engine.infrastructure.repository.storable.ErrorEventStorable;
import org.actio.engine.infrastructure.repository.translator.ErrorEventStorableTranslator;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Listen for uncaught errors and store the information
 * 
 * @author quirino.brizi
 *
 */
public class ErrorEventListener implements ActivitiEventListener {

    @Autowired
    private ErrorEventRepository errorEventRepository;
    @Autowired
    private ErrorEventStorableTranslator errorEventStorableTranslator;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.activiti.engine.delegate.event.ActivitiEventListener#onEvent(org.
     * activiti.engine.delegate.event.ActivitiEvent)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onEvent(ActivitiEvent event) {
        persistEventIfNeeded(event);
    }

    private void persistEventIfNeeded(ActivitiEvent event) {
        if (ActivitiEventType.UNCAUGHT_BPMN_ERROR.equals(event.getType())) {
            ErrorEventStorable storable = errorEventStorableTranslator.translate((BpmnErrorEvent) event);
            errorEventRepository.saveAndFlush(storable);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.delegate.event.ActivitiEventListener#
     * isFailOnException()
     */
    @Override
    public boolean isFailOnException() {
        return false;
    }

}
