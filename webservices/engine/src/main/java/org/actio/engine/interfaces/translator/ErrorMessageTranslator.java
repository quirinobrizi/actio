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
package org.actio.engine.interfaces.translator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.actio.commons.message.bpmn.ErrorMessage;
import org.actio.engine.domain.model.bpmn.Error;
import org.actio.engine.infrastructure.Translator;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class ErrorMessageTranslator implements Translator<ErrorMessage, Error> {

    @Override
    public ErrorMessage translate(Error error) {
        return new ErrorMessage(error.getErrorType(), error.getErrorMessage(), error.getTimestamp());
    }

    @Override
    public Collection<ErrorMessage> translate(Collection<Error> errors) {
        Set<ErrorMessage> answer = new HashSet<>();
        for (Error error : errors) {
            answer.add(translate(error));
        }
        return answer;
    }

}
