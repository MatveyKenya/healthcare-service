package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestMedicalServiceImpl {

    PatientInfoRepository patientInfoRepository;
    SendAlertService sendAlertService;
    PatientInfo patientInfo;
    MedicalService medicalService;

    @BeforeEach
    void initServices() {
        patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        sendAlertService = Mockito.spy(SendAlertServiceImpl.class);
        patientInfo = new PatientInfo("1", "Иван", "Петров",
                LocalDate.of(1980, 11, 26),
                new HealthInfo(
                        new BigDecimal("36.65"),
                        new BloodPressure(120, 80)));

        Mockito
                .when(patientInfoRepository.add(patientInfo))
                .thenReturn("1");
        Mockito
                .when(patientInfoRepository.getById("1"))
                .thenReturn(patientInfo);

        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
    }

    @Test
    void checkBloodPressureWithErrorTest() {
        BloodPressure currentPressure = new BloodPressure(60, 120);
        medicalService.checkBloodPressure(patientInfoRepository.add(patientInfo), currentPressure);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertNotEquals(argumentCaptor.getValue(), "");
    }

    @Test
    void checkNormalBloodPressureTest() {
        BloodPressure currentPressure = new BloodPressure(120, 80);
        medicalService.checkBloodPressure(patientInfoRepository.add(patientInfo), currentPressure);
        Mockito.verify(sendAlertService, Mockito.times(0)).send("Warning, patient with id: 1, need help");
    }

    @Test
    void checkTemperatureWithErrorTest() {

        BigDecimal currentTemperature = new BigDecimal("30");
        medicalService.checkTemperature(patientInfoRepository.add(patientInfo), currentTemperature);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertNotEquals(argumentCaptor.getValue(), "");
    }

    @Test
    void checkNormalTemperatureTest() {

        BigDecimal currentTemperature = new BigDecimal("36");
        medicalService.checkTemperature(patientInfoRepository.add(patientInfo), currentTemperature);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sendAlertService, Mockito.times(0)).send("Warning, patient with id: 1, need help");
    }


}
