package com.openclassrooms.realestatemanager.utils;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.*;

import static com.google.common.truth.Truth.assertThat;

//@RunWith(MockitoJUnitRunner.class)
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
    public void testGetTodayDateTest() {
//        Calendar calendar = new GregorianCalendar(2018, 0, 13);
//        long fakeToday = calendar.getTimeInMillis();
//
//        Calendar c = mock(Calendar.class);
//        when(c.getTimeInMillis()).thenReturn(fakeToday);

        Calendar fakeToday = Calendar.getInstance();
        fakeToday.set(2018, Calendar.MARCH, 13);
        PowerMockito.mockStatic(Calendar.class);
        Mockito.when(Calendar.getInstance()).thenReturn(fakeToday);

//        Utils myTestedUtilsClass = new Utils();
        String result = Utils.getTodayDate();
        assertThat(result).isEqualTo("13/03/2018");
    }

    @Test
    public void calculateMensualityShouldReturn1265Test() {
        String result = String.valueOf(Utils.calculateMensuality(200000,4.5,20));
        assertThat(result).isEqualTo("1265");
    }

}