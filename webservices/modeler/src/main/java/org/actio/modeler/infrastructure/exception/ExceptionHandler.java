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
package org.actio.modeler.infrastructure.exception;

import java.io.IOException;
import java.util.Map;

import org.actio.commons.message.ErrorMessage;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice(basePackages = { "org.actio.modeler.interfaces" })
public class ExceptionHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorMessage> handle(Exception e) throws Exception {
        return handleHttpStatusCodeException(e);
    }

    private ResponseEntity<ErrorMessage> handleHttpStatusCodeException(Exception e) throws IOException {
        int index = ExceptionUtils.indexOfType(e, HttpStatusCodeException.class);
        if (index >= 0) {
            // we have HttpClientErrorException
            HttpStatusCodeException exc = (HttpStatusCodeException) ExceptionUtils.getThrowableList(e).get(index);
            String message = extractMessage(exc);
            return new ResponseEntity<ErrorMessage>(new ErrorMessage(exc.getRawStatusCode(), message), exc.getStatusCode());
        }
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(500, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String extractMessage(HttpStatusCodeException exc) throws IOException, JsonParseException, JsonMappingException {
        String message;
        Map<String, String> body = this.objectMapper.readValue(exc.getResponseBodyAsByteArray(), new TypeReference<Map<String, String>>() {
        });
        if (body.containsKey("exception")) {
            message = body.get("exception");
        } else {
            message = exc.getStatusText();
        }
        return message;
    }
}
