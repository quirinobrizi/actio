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

import org.actio.engine.domain.model.bpmn.Action;
import org.actio.engine.domain.model.bpmn.Bpmn;
import org.actio.engine.domain.model.bpmn.Inputs;
import org.actio.engine.domain.model.bpmn.process.ProcessId;

/**
 * @author quirino.brizi
 *
 */
public interface ProcessService {

    /**
     * Update the status of a service;
     * 
     * @param action
     *            the action to take to perform the update
     * @param processId
     *            the process to update
     * @param inputs
     *            the inputs to provide to the process
     * @return the process updated to its new status.
     */
    Bpmn updateState(Action action, ProcessId processId, Inputs inputs);
}
