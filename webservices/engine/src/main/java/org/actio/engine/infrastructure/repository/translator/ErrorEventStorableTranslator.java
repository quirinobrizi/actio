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
package org.actio.engine.infrastructure.repository.translator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.actio.engine.domain.model.bpmn.Error;
import org.actio.engine.infrastructure.repository.BpmnErrorEvent;
import org.actio.engine.infrastructure.repository.storable.ErrorEventStorable;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class ErrorEventStorableTranslator {

    public ErrorEventStorable translate(BpmnErrorEvent event) {
        ErrorEventStorable answer = new ErrorEventStorable();
        answer.setProcessInstanceId(event.getProcessInstanceId());
        answer.setProcessDefinitionKey(event.getProcessDefinitionKey());
        answer.setErrorType(event.getErrorType());
        answer.setErrorMessage(event.getErrorMessage());
        answer.setTimestamp(System.currentTimeMillis());
        return answer;
    }

    public Collection<Error> translate(List<ErrorEventStorable> storables) {
        Set<Error> answer = new HashSet<>();
        for (ErrorEventStorable storable : storables) {
            answer.add(translate(storable));
        }
        return answer;
    }

    public Error translate(ErrorEventStorable storable) {
        return new Error(storable.getErrorType(), storable.getErrorMessage(), storable.getTimestamp());
    }

}
