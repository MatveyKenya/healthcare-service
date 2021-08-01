package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;

public class TestMedicalServiceImpl {

    @Test
    void test_checkBloodPressure(){
        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById("123"))
                .thenReturn(new PatientInfo("123", null, null, null,
                        new HealthInfo(BigDecimal.valueOf(37), new BloodPressure(120,80))));

//        SendAlertServiceImpl service = Mockito.mock(SendAlertServiceImpl.class);
//        Mockito.verify(service.Mockito.times(1).send)

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, new SendAlertServiceImpl());
        medicalService.checkBloodPressure("123",new BloodPressure(120, 80));
    }
}
