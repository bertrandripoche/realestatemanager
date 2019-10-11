package com.openclassrooms.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Flat;
import com.openclassrooms.realestatemanager.database.dao.FlatDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class FlatDaoTest {

    // FOR DATA
    private RealEstateManagerDatabase database;
    private static long AGENT_ID = 1;
    private static Agent AGENT_DEMO = new Agent(1,"Philippe", "Dubois","Philippe.Dubois@gmail.com");
    private static Flat NEW_Flat1 = new Flat(2, "Urbain et stylé", "Appartement", 480000, 67, 3 , 2, 1, 5, "rue des Petites Écuries", 75010, "Paris",1);
    private static Flat NEW_Flat2 = new Flat(3, "Familial de luxe", "Appartement", 1560000, 138, 6 , 4, 2, 121, "rue de Rivoli", 75001, "Paris",1);
    private static Flat NEW_Flat3 = new Flat(4, "Pour grande famille","Appartement", 980000, 108, 4 , 2, 2, 34, "rue de Trévise", 75009, "Paris",1);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    @Test
    public void insertAndGetAgent() throws InterruptedException {
        // BEFORE : Adding a new user
        this.database.mAgentDao().createAgent(AGENT_DEMO);
        // TEST
        Agent agent = LiveDataTestUtil.getValue(this.database.mAgentDao().getAgent(AGENT_ID));

        assertTrue(agent.getLastname().equals(AGENT_DEMO.getLastname()) && agent.getId() == AGENT_ID);
    }

    @Test
    public void getFlatsWhenNoFlatInserted() throws InterruptedException {
        // TEST
        List<Flat> Flats = LiveDataTestUtil.getValue(this.database.mFlatDao().getFlats(AGENT_ID));
        assertTrue(Flats.isEmpty());
    }

    @Test
    public void insertAndGetFlats() throws InterruptedException {
        // BEFORE : Adding demo user & demo Flats

        this.database.mAgentDao().createAgent(AGENT_DEMO);
        this.database.mFlatDao().insertFlat(NEW_Flat1);
        this.database.mFlatDao().insertFlat(NEW_Flat2);
        this.database.mFlatDao().insertFlat(NEW_Flat3);

        // TEST
        List<Flat> Flats = LiveDataTestUtil.getValue(this.database.mFlatDao().getFlats(AGENT_ID));
        assertTrue(Flats.size() == 3);
    }

    @Test
    public void insertAndUpdateFlat() throws InterruptedException {
        // BEFORE : Adding demo user & demo Flats. Next, update Flat added & re-save it
        this.database.mAgentDao().createAgent(AGENT_DEMO);
        this.database.mFlatDao().insertFlat(NEW_Flat1);
        Flat FlatAdded = LiveDataTestUtil.getValue(this.database.mFlatDao().getFlats(AGENT_ID)).get(0);
        FlatAdded.setBathroom(3);
        this.database.mFlatDao().updateFlat(FlatAdded);

        //TEST
        List<Flat> Flats = LiveDataTestUtil.getValue(this.database.mFlatDao().getFlats(AGENT_ID));
        assertTrue(Flats.size() == 1 && Flats.get(0).getBathroom() == 3);
    }

    @Test
    public void insertAndDeleteFlat() throws InterruptedException {
        // BEFORE : Adding demo user & demo Flat. Next, get the Flat added & delete it.
        this.database.mAgentDao().createAgent(AGENT_DEMO);
        this.database.mFlatDao().insertFlat(NEW_Flat1);
        Flat FlatAdded = LiveDataTestUtil.getValue(this.database.mFlatDao().getFlats(AGENT_ID)).get(0);
        this.database.mFlatDao().deleteFlat(FlatAdded.getId());

        //TEST
        List<Flat> Flats = LiveDataTestUtil.getValue(this.database.mFlatDao().getFlats(AGENT_ID));
        assertTrue(Flats.isEmpty());
    }
}
