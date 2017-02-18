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
package org.actio.modeler.interfaces;

import org.actio.commons.message.process.ProcessMessage;
import org.actio.commons.message.process.UpdateProcessStateRequestMessage;
import org.actio.modeler.app.ProcessService;
import org.actio.modeler.domain.model.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author quirino.brizi
 *
 */
@RestController
@RequestMapping(path = "/processes", produces = MediaType.APPLICATION_JSON_VALUE)
public class Processes {

    @Autowired
    private ProcessService metricsService;

    @RequestMapping(value = "/metrics", method = RequestMethod.GET)
    public Metrics getMetrics() {
        return metricsService.getMetrics();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ProcessMessage updateProcess(@RequestBody UpdateProcessStateRequestMessage message) {
        return metricsService.update(message);
    }

    @RequestMapping(path = "/{processId}", method = RequestMethod.DELETE)
    public void deleteProcess(@PathVariable(name = "processId") String process) {
        metricsService.delete(process);
    }
}
