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
package org.actio.modeler.infrastructure.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.actio.commons.message.metrics.MetricsMessage;
import org.actio.modeler.domain.model.Metrics;
import org.actio.modeler.domain.model.ProcessMetrics;
import org.actio.modeler.domain.model.VersionMetrics;
import org.actio.modeler.infrastructure.config.ModelerConfigurationProperties;
import org.actio.modeler.infrastructure.config.ModelerConfigurationProperties.Engine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class ProcesRepositoryImplTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ModelerConfigurationProperties configuration;

    @InjectMocks
    private ProcessRepositoryImpl testObj;

    @Mock
    private Engine engine;

    @Test
    public void testGet() throws Exception {
        when(configuration.getEngine()).thenReturn(engine);
        when(engine.getUrlFormat()).thenReturn("http://actio.org/{activiti}");
        when(restTemplate.getForObject("http://actio.org/{activiti}", MetricsMessage.class, "activiti")).thenReturn(metrics());
        // act
        Metrics metrics = testObj.getProcessesMetrics();
        // assert
        assertEquals(11622, metrics.getCompletedActivities().intValue());
        assertEquals(1241, metrics.getProcessDefinitionCount().intValue());
        assertEquals(0, metrics.getCachedProcessDefinitionCount().intValue());
        ArrayList<ProcessMetrics> processes = new ArrayList<>(metrics.getProcessesMetrics());
        ProcessMetrics processMetrics = processes.get(processes.indexOf(new ProcessMetrics("PLATFORM_AD_1883_RM_onboard")));
        List<VersionMetrics> versions = processMetrics.getVersions();
        VersionMetrics versionMetrics = versions.get(versions.indexOf(new VersionMetrics("v1")));
        assertEquals(10, versionMetrics.getCompleted().intValue());
        assertEquals(0, versionMetrics.getRunning().intValue());
    }

    private MetricsMessage metrics() throws Exception {
        ResourceLoader loader = new DefaultResourceLoader();
        return new ObjectMapper().readValue(loader.getResource("classpath:metrics.json").getFile(), MetricsMessage.class);
    }

}
