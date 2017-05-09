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
package org.actio.engine.interfaces.parser;

import java.net.URI;

import javax.xml.stream.XMLStreamReader;

import org.actio.engine.infrastructure.Uri;
import org.actio.engine.infrastructure.exception.InternalServerErrorException;
import org.activiti.bpmn.converter.child.BaseChildElementParser;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ScriptTask;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author quirino.brizi
 *
 */
public class ScriptResourceParser extends BaseChildElementParser {

    private RestTemplate template = new RestTemplate();

    @Override
    public String getElementName() {
        return "resource";
    }

    @Override
    public void parseChildElement(XMLStreamReader xtr, BaseElement parentElement, BpmnModel model) throws Exception {
        if (!ScriptTask.class.isAssignableFrom(parentElement.getClass())) {
            return;
        }
        Uri uri = Uri.from(xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, "resource"));
        String script = retrieveScript(uri);
        ((ScriptTask) parentElement).setScript(script);
    }

    private String retrieveScript(Uri uri) {
        URI url = uri.removeQueryString();
        RequestEntity<?> requestEntity = RequestEntity.get(url).accept(MediaType.TEXT_PLAIN).header(HttpHeaders.AUTHORIZATION,
                String.format("%s %s", uri.getQueryParameter("authType"), uri.getQueryParameter("authToken"))).build();
        ResponseEntity<String> entity = template.exchange(requestEntity, String.class);
        if (entity.getStatusCode().is2xxSuccessful() && entity.hasBody()) {
            return entity.getBody();
        }
        throw InternalServerErrorException.newInstance("unable to retrieve script resource from [%s]", url);
    }

}
