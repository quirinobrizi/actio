package org.actio.modeler.infrastructure.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.actio.commons.message.metrics.MetricsMessage;
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
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Mock
	private ProcessMessage processMesage;
	
	@Test
	public void testAdd() {
		when(configuration.getEngine()).thenReturn(engine);
		when(engine.getUrlFormat()).thenReturn("http://actio.org/{activiti}");
		when(message.getId()).thenReturn("myId");
		when(restTemplate.postForObject("http://actio.org/{activiti}", message, ModelMessage.class, "models"))
				.thenReturn(message);
		when(restTemplate.postForObject(Mockito.eq("http://actio.org/{activiti}"), captor.capture(), Mockito.eq(ProcessMessage.class), Mockito.eq("processes")))
				.thenReturn(processMesage);
		ModelMessage modelMessage = testObj.add(message);
		// assert
		assertEquals(message, modelMessage);
		assertEquals("myId", captor.getValue().getModelId());
	}

	@Test
	public void testGetAllModels() {
//		fail("Not yet implemented");
	}

}
