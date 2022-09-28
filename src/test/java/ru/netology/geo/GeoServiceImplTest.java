package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import java.util.stream.Stream;

class GeoServiceImplTest {

    private GeoServiceImpl geoService;


    @BeforeEach
    public void init() {
        geoService = new GeoServiceImpl();
    }

    @Test
    public void testByCoordinates_whenNotImplemented() {
        Assertions.assertThrows(RuntimeException.class, () -> geoService.byCoordinates(1, 2));
    }

    @ParameterizedTest
    @MethodSource("ipToLocation")
    public void testByIp_whenLOCALHOST_equalsIp(String ip, Location expected) {
        Location result = geoService.byIp(ip);
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> ipToLocation() {
        return Stream.of(
                Arguments.arguments(GeoServiceImpl.LOCALHOST,
                        new Location(null, null, null, 0)),
                Arguments.arguments(GeoServiceImpl.MOSCOW_IP,
                        new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.arguments(GeoServiceImpl.NEW_YORK_IP,
                        new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.arguments("172.0.0.1",
                        new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.arguments("96.0.0.1",
                        new Location("New York", Country.USA, null,  0))
        );
    }
}