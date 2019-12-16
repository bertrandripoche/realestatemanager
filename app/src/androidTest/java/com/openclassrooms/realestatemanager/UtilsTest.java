package com.openclassrooms.realestatemanager;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Flat;
import com.openclassrooms.realestatemanager.database.dao.FlatDao;
import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
//@RunWith(AndroidJUnit4.class)
public class UtilsTest {

    @Test
    public void checkConnectionAgainstGoogleSite() throws Exception {
        String google = "https://www.google.com";
        Context context = mock(Context.class);

        if (Utils.isInternetAvailable(context)) System.out.println("OK");
        else System.out.println("Not OK");
   }


}
