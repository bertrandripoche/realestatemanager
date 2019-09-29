package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.google.common.truth.Truth.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
        Calendar calendar = new GregorianCalendar(2018, 0, 13);
        long today = calendar.getTimeInMillis();

        Calendar c = mock(Calendar.class);
        when(c.getTimeInMillis()).thenReturn(today);

        Utils myTestedUtilsClass = new Utils();
        String result = myTestedUtilsClass.getTodayDate();
        assertThat(result).isEqualTo("03/03/2018");
    }
}