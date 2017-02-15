package org.actio.modeler.infrastructure.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.actio.commons.message.model.ModelMessage;
import org.actio.commons.message.process.DeployProcessRequestMessage;
import org.actio.commons.message.process.ProcessMessage;
import org.actio.modeler.infrastructure.config.ModelerConfigurationProperties;
import org.actio.modeler.infrastructure.config.ModelerConfigurationProperties.Engine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ModelRepositoryImplTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ModelerConfigurationProperties configuration;

    @InjectMocks
    private ModelRepositoryImpl testObj;

    @Mock
    private ModelMessage message;
    @Mock
    private Engine engine;

    @Captor
    private ArgumentCaptor<DeployProcessRequestMessage> captor;
    @Captor
    private ArgumentCaptor<RequestEntity<Void>> requestEntityCaptor;

    @Mock
    private ProcessMessage processMesage;
    @Mock
    private ResponseEntity<List<ModelMessage>> response;

    @Test
    public void testAdd() {
        when(configuration.getEngine()).thenReturn(engine);
        when(engine.getUrlFormat()).thenReturn("http://actio.org/{activiti}");
        when(message.getId()).thenReturn("myId");
        when(restTemplate.postForObject("http://actio.org/{activiti}", message, ModelMessage.class, "models")).thenReturn(message);
        when(restTemplate.postForObject(Mockito.eq("http://actio.org/{activiti}"), captor.capture(), Mockito.eq(ProcessMessage.class),
                Mockito.eq("processes"))).thenReturn(processMesage);
        ModelMessage modelMessage = testObj.add(message);
        // assert
        assertEquals(message, modelMessage);
        assertEquals("myId", captor.getValue().getModelId());
    }

    @Test
    public void testGetAllModels() {
        when(configuration.getEngine()).thenReturn(engine);
        when(engine.getUrlFormat()).thenReturn("http://actio.org/{activiti}");
        ParameterizedTypeReference<List<ModelMessage>> responseType = new ParameterizedTypeReference<List<ModelMessage>>() {
        };
        when(restTemplate.exchange(requestEntityCaptor.capture(), Mockito.eq(responseType))).thenReturn(response);
        List<ModelMessage> expected = Arrays.asList(message);
        when(response.getBody()).thenReturn(expected);
        // act
        List<ModelMessage> actual = testObj.getAllModels();
        // assert
        assertEquals(expected, actual);
        RequestEntity<Void> requestEntity = requestEntityCaptor.getValue();
        assertEquals(HttpMethod.GET, requestEntity.getMethod());
        assertEquals("http://actio.org/models", requestEntity.getUrl().toString());
    }

}
