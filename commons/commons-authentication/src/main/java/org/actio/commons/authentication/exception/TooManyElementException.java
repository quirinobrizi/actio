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
package org.actio.commons.authentication.exception;

import org.activiti.engine.ActivitiException;

/**
 * Exception thrown when more elements have been found than expected
 * 
 * @author quirino.brizi
 *
 */
public class TooManyElementException extends ActivitiException {

    private static final long serialVersionUID = -5706163774178154859L;

    public static TooManyElementException newInstance(String message) {
        return new TooManyElementException(message);
    }

    private TooManyElementException(String message) {
        super(message);
    }

}
