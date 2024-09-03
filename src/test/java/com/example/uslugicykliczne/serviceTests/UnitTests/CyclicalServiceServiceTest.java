package com.example.uslugicykliczne.serviceTests.UnitTests;

import com.example.uslugicykliczne.repo.CustomerRepo;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.repo.DysponentRepo;
import com.example.uslugicykliczne.services.CyclicalServiceService;
import com.example.uslugicykliczne.services.SchedulingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CyclicalServiceServiceTest {
    @Mock
    private SchedulingService schedulingServiceMock;
    @Mock
    private DysponentRepo dysponentRepoMock;
    @Mock
    private CustomerRepo customerRepoMock;
    @Mock
    private CyclicalServiceRepo cyclicalServiceRepoMock;
    @InjectMocks
    private CyclicalServiceService cyclicalServiceService;
    AutoCloseable autoCloseable;
    @BeforeEach
    public void setUp() {
        autoCloseable  = MockitoAnnotations.openMocks(this);
    }

    @AfterEach()
    public void close() throws Exception{
        autoCloseable.close();
    }


    @Test
    @DisplayName("Test if renewal works correctly when db holds no entity with id provided by user")
    public void renewFailureDB(){
        when(cyclicalServiceRepoMock.findById(any())).thenReturn(Optional.empty());

        ResponseEntity<String> responseEntity = cyclicalServiceService.renewCyclicalService(0);

        assertEquals(ResponseEntity.badRequest().body("Can't find the task you try to renew"),responseEntity);
    }

    @Test
    @DisplayName("renewal reminder wasn't sent for this entity ")
    public void renewSuccessNotSent(){
        CyclicalServiceEntity cyclicalServiceEntity = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().plusYears(1),
                Period.of(1,0,0)
        );
        cyclicalServiceEntity.setId(0);
        when(cyclicalServiceRepoMock.findById(any())).thenReturn(Optional.of(cyclicalServiceEntity));

        ResponseEntity<String> responseEntity = cyclicalServiceService.renewCyclicalService(0);

        verify(schedulingServiceMock,times(1)).trySchedulingReminderWhenUpdated(cyclicalServiceEntity);
        assertEquals(ResponseEntity.ok().body("The task was successfully renewed"),responseEntity);

    }

    @Test
    @DisplayName("renewal  reminder was sent for this entity ")
    public void renewSuccessSent(){
        CyclicalServiceEntity cyclicalServiceEntity = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().plusYears(1),
                Period.of(1,0,0)
        );
        cyclicalServiceEntity.setId(0);
        cyclicalServiceEntity.setRenewalMessageSent(true);
        when(cyclicalServiceRepoMock.findById(any())).thenReturn(Optional.of(cyclicalServiceEntity));

        ResponseEntity<String> responseEntity = cyclicalServiceService.renewCyclicalService(0);

        verify(schedulingServiceMock,times(1)).trySchedulingReminderWhenInserted(cyclicalServiceEntity);
        assertEquals(ResponseEntity.ok().body("The task was successfully renewed"),responseEntity);

    }
}
