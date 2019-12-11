package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.provider.FlatContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(AndroidJUnit4.class)
public class FlatContentProviderTest {
    // FOR DATA
    private ContentResolver mContentResolver;

    // DATA SET FOR TEST
    private static long AGENT_ID = 0;

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getContext().getContentResolver();
    }

    @Test
    public void getFlatsWhenNoFlatInserted() {
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(FlatContentProvider.URI_FLAT, AGENT_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        cursor.close();
    }

    @Test
    public void insertAndGetFlat() {
        // BEFORE : Adding demo flat
        final Uri userUri = mContentResolver.insert(FlatContentProvider.URI_FLAT, generateFlat());
        // TEST
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(FlatContentProvider.URI_FLAT, AGENT_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("summary")), is("Appart de rêve !"));
    }

    // ---
    private ContentValues generateFlat(){
        final ContentValues values = new ContentValues();
        values.put("summary", "Appart de rêve !");
        values.put("description", "Un appartement comme on en voit jamais");
        values.put("price", "1500000");
        values.put("surface", "200");
        values.put("cityAddress", "Monaco");
        values.put("agentId", "0");
        return values;
    }
}
