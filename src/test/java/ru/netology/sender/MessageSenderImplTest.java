package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MessageSenderImplTest {

    private MessageSenderImpl messageSender;

    private GeoServiceImpl geoService;

    private LocalizationServiceImpl localizationService;

    @BeforeEach
    public void init() {
        geoService = Mockito.spy(GeoServiceImpl.class);
        localizationService = Mockito.spy(LocalizationServiceImpl.class);
        messageSender = new MessageSenderImpl(geoService, localizationService);
    }

    @ParameterizedTest
    @ValueSource(strings = {GeoServiceImpl.MOSCOW_IP, "172.0.0.1"})
    void test_ifIP_belongsRussia_messageIsRussian(String ipAddress) {
        // init
        String expected = "Добро пожаловать";
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ipAddress);

        // execution
        String actual = messageSender.send(headers);

        // verify
        Assertions.assertEquals(expected, actual);
        Mockito.verify(geoService, Mockito.times(1))
                .byIp(ArgumentMatchers.anyString());
        Mockito.verify(localizationService, Mockito.times(1))
                .locale(ArgumentMatchers.any(Country.class));
        Mockito.verify(localizationService, Mockito.times(1))
                .locale(Country.RUSSIA);
    }

    @Test
    void test_ifIP_belongsRussia_messageIsRussian_mockingMethod() {
        // init
        String expected = "Русский";
        String ipAddress = GeoServiceImpl.MOSCOW_IP;
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ipAddress);

        Mockito.when(geoService.byIp(ipAddress))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));

        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn(expected);

        // execution
        String actual = messageSender.send(headers);

        // verify
        Assertions.assertEquals(expected, actual);
        Mockito.verify(geoService, Mockito.times(1))
                .byIp(ArgumentMatchers.anyString());
        Mockito.verify(localizationService, Mockito.times(1))
                .locale(ArgumentMatchers.any(Country.class));
        Mockito.verify(localizationService, Mockito.times(1))
                .locale(Country.RUSSIA);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void test_ifIPisAbsent_messageIsEnglish(String ipAddress) {  //null ""
        // init
        String expected = "Welcome";
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ipAddress);

        // execution
        String actual = messageSender.send(headers);

        // verify
        Assertions.assertEquals(expected, actual);

        Mockito.verify(localizationService, Mockito.times(1))
                .locale(Country.USA);
    }

    @Test
    void test_ifLocationIsNull_messageIsEnglish() {
        String expected = "Welcome";
        Map<String, String> headers = new HashMap<>();
        String ipAddress = "10.44.0.0";
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ipAddress);

        ResultCaptor<String> resultCaptor = new ResultCaptor<>();

        Mockito.doAnswer(resultCaptor).when(geoService).byIp(ipAddress);

        String actual = messageSender.send(headers);

        Assertions.assertEquals(expected, actual);
        Assertions.assertNull(resultCaptor.getResult());
        Mockito.verify(geoService, Mockito.times(1))
            .byIp(ipAddress);

    }
}