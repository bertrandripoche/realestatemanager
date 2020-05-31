package com.openclassrooms.realestatemanager.utils;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;

import static com.google.common.truth.Truth.assertThat;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Utils.class)
public class UtilsTest {

    @Test
    public void convert100DollarToEuroShouldReturn81Test() {
        Utils myTestedUtilsClass = new Utils();
        String result = String.valueOf(myTestedUtilsClass.convertDollarToEuro(100));
        assertThat(result).isEqualTo("81");
    }

    @Test
    public void convert0DollarToEuroShouldReturn0Test() {
        Utils myTestedUtilsClass = new Utils();
        String result = String.valueOf(myTestedUtilsClass.convertDollarToEuro(0));
        assertThat(result).isEqualTo("0");
    }

    @Test
    public void convertNeg100DollarToEuroShouldReturnNeg81Test() {
        String result = String.valueOf(Utils.convertDollarToEuro(-100));
        assertThat(result).isEqualTo("-81");
    }

    @Test
    public void convert81EuroToDollarShouldReturn100Test() {
        String result = String.valueOf(Utils.convertEuroToDollar(81));
        assertThat(result).isEqualTo("100");
    }

    @Test
    public void convert0EuroToDollarShouldReturn0Test() {
        String result = String.valueOf(Utils.convertEuroToDollar(0));
        assertThat(result).isEqualTo("0");
    }

    @Test
    public void convertNeg81EuroToDollarShouldReturnNeg100Test() {
        String result = String.valueOf(Utils.convertEuroToDollar(-81));
        assertThat(result).isEqualTo("-100");
    }

    @Test
    public void getTodayDateTest() throws Exception {

        Calendar fakeToday = Calendar.getInstance();
        fakeToday.set(2018, Calendar.MARCH, 13);
        mockStatic(Calendar.class);

        Mockito.when(Calendar.getInstance()).thenReturn(fakeToday);

        String result = Utils.getTodayDate();
        assertThat(result).isEqualTo("13/03/2018");
    }

    @Test
    public void calculateMensualityShouldReturn1265Test() {
        String result = String.valueOf(Utils.calculateMensuality(200000,4.5,20));
        assertThat(result).isEqualTo("1265");
    }

}