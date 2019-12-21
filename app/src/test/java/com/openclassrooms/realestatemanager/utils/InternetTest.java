package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowNetworkInfo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class InternetTest {

    @Test
    public void isInternetAvailableShouldReturnFalseIfNotConnected() {
        shadowOf((ConnectivityManager) RuntimeEnvironment.systemContext
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .setActiveNetworkInfo(ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.DISCONNECTED, ConnectivityManager.TYPE_WIFI, 0, false, NetworkInfo.State.DISCONNECTED));

        assertFalse(Utils.isInternetAvailable(RuntimeEnvironment.systemContext));
    }

    @Test
    public void isInternetAvailableShouldReturnTrueIfConnected() {
        shadowOf((ConnectivityManager) RuntimeEnvironment.systemContext
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .setActiveNetworkInfo(ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.CONNECTED, ConnectivityManager.TYPE_WIFI, 0, true, NetworkInfo.State.CONNECTED));

        assertTrue(Utils.isInternetAvailable(RuntimeEnvironment.systemContext));
    }
}
