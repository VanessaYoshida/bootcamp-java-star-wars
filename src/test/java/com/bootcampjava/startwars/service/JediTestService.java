package com.bootcampjava.startwars.service;

import com.bootcampjava.startwars.model.Jedi;
import com.bootcampjava.startwars.repository.JediRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JediTestService {

    @Autowired
    private JediService jediService;

    @MockBean
    private JediRepositoryImpl jediRepository;


    @Test
    @DisplayName("Should return Jedi with success")
    public void testFindBySuccess() {

        // cenario
        Jedi mockJedi = new Jedi(1, "Jedi Name", 10, 1);
        Mockito.doReturn(Optional.of(mockJedi)).when(jediRepository).findById(1);

        // execucao
        Optional<Jedi> returnedJedi  = jediService.findById(1);

        // assert
        Assertions.assertTrue(returnedJedi.isPresent(), "Jedi was found");
        Assertions.assertSame(returnedJedi.get(), mockJedi, "Jedis must be the same");
    }

    // TODO: Criar teste de erro NOT FOUND
    @Test
    @DisplayName("Should return Jedi was not found")
    public void testNotFoundJedi(){

        //cenario
        Jedi mockJedi = new Jedi(2, "Luke Skywalker", 100, 1);
        Mockito.doReturn(Optional.of(mockJedi)).when(jediRepository).findById(2);

        //execucao
        Optional<Jedi> returnedJedi = jediService.findById(1);

        //assert
        Assertions.assertFalse(returnedJedi.isPresent(), "Jedi was not found");
        Assertions.assertNotSame(returnedJedi, mockJedi, "Jedis must be the same");
    }

    // TODO: Criar um teste pro findAll();
    @Test
    @DisplayName("Should return all Jedis")
    public void testReturnAllJedis(){

        //cenario
        Jedi mockJedi = new Jedi(2, "Luke Skywalker", 100, 1);
        Jedi mockJedi2 = new Jedi(3, "Yoda", 500, 1);
        Mockito.doReturn(Optional.of(mockJedi)).when(jediRepository).findAll();

        //execucao
        List<Jedi> returnedJedi = jediService.findAll();

        //assert
//        Assertions.assertTrue(returnedJedi., "Jedi was found");
//        Assertions.assertSame(returnedJedi.get(), mockJedi, "Jedis must be the same");


//        @Test
//        public void testProcess(){
//            final List<SoapEvent> soapEvents = new ArrayList<SoapEvent>();
//            for(int index = 0; index < 3; index++){
//                final SoapEvent soapEvent = SoapEventGenerator.generateSoapEvent();
//                soapEvents.add(soapEvent);
//            }
//
//            Mockito.when(repository.findAll()).thenReturn(soapEvents);
//
//            final ReadAllSoapEventInput input = ReadAllSoapEventInput.builder().build();
//            final ServiceTask<ReadAllSoapEventInput> serviceTask = new ServiceTask<ReadAllSoapEventInput>(input);
//            final ServiceResult<ReadAllSoapEventOutput> serviceResult = service.process(serviceTask);
//            final ReadAllSoapEventOutput output = serviceResult.getOutput();
//
//            Assert.assertEquals(soapEvents.size(), output.getSoapEvents().size());
//
//            for(int index = 0; index < 3; index++){
//                final SoapEvent soapEvent = soapEvents.get(index);
//                final SoapEvent returnedSoapEvent = output.getSoapEvents().get(index);
//
//                Assert.assertEquals(soapEvent.getId(), returnedSoapEvent.getId());
//                Assert.assertEquals(soapEvent.getOperationId(), returnedSoapEvent.getOperationId());
//                Assert.assertEquals(soapEvent.getPortId(), returnedSoapEvent.getPortId());
//                Assert.assertEquals(soapEvent.getProjectId(), returnedSoapEvent.getProjectId());
//            }
//        }
//    }
    }
}
