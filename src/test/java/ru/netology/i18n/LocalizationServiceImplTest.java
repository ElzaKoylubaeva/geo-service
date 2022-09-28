package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import ru.netology.entity.Country;

import static org.junit.jupiter.api.Assertions.*;

class LocalizationServiceImplTest {

    private static LocalizationService localizationService;

    @BeforeAll
    public static void init() {
        localizationService = new LocalizationServiceImpl();
    }


    @Test
    public void testLocale_whenRussia_messageShouldBeRussian() {
        //init
        Country country = Country.RUSSIA;

        //execute
        String actual = localizationService.locale(country);

        //check
        Assertions.assertEquals("Добро пожаловать", actual);
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(value = Country.class, names = {"USA", "BRAZIL", "GERMANY"})
    public void testLocale_whenIsNotRussia_messageShouldBeEnglish(Country country) {

        //execute
        String actual = localizationService.locale(country);

        //check
        Assertions.assertEquals("Welcome", actual);
    }
}