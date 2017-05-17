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
package org.actio.engine.infrastructure.command;

import java.util.concurrent.ExecutorService;

/**
 * @author quirino.brizi
 *
 */
public interface Command<I, O> {

    /**
     * Execute the command
     * 
     * @param bpmnId
     *            the BPMN identifier
     * @param inputs
     *            the command inputs
     * @return the command execution result.
     */
    O execute(String bpmnId, I inputs);

    /**
     * Execute the command
     * 
     * @param bpmnId
     *            the BPMN identifier
     * @param inputs
     *            the command inputs
     * @param executor
     *            the async executor
     * @return the command execution result.
     */
    O execute(String bpmnId, I inputs, ExecutorService executor);

    /**
     * Define the type of this command
     * 
     * @return
     */
    Type type();

    /**
     * Defines the available command types.
     * 
     * @author quirino.brizi
     *
     */
    public enum Type {
        START_PROCESS_INSTANCE;
    }
}
