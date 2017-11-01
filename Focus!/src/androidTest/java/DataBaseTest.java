import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.pk.example.AppDatabase;
import com.pk.example.dao.MinNotificationDao;
import com.pk.example.dao.PrevNotificationDao;
import com.pk.example.dao.ProfileDao;
import com.pk.example.dao.ScheduleDao;
import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.PrevNotificationEntity;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by williamxu on 10/29/17.
 */

@RunWith(AndroidJUnit4.class)
public class DataBaseTest {

    private MinNotificationDao minNotificationDao;
    private PrevNotificationDao prevNotificationDao;
    private ProfileDao profileDao;
    private ScheduleDao scheduleDao;
    private AppDatabase db;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        minNotificationDao = db.minNotificationDao();
        prevNotificationDao = db.prevNotificationDao();
        profileDao = db.profileDao();
        scheduleDao = db.scheduleDao();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void shouldCreateDatabase() {
        assertNotNull(db);
    }

    @Test
    public void shouldCreateDao() {
        assertNotNull(prevNotificationDao);
        assertNotNull(profileDao);
        assertNotNull(scheduleDao);
        assertNotNull(minNotificationDao);
    }

    @Test
    public void shouldInsertPrevNotification() {
        PrevNotificationEntity prevNotification = new PrevNotificationEntity();
        prevNotification.setAppName("app");
        prevNotification.setDate(new Date());
        prevNotification.setNotificationContext("this is the notification");
        prevNotificationDao.insert(prevNotification);
        List notifs = prevNotificationDao.loadAllPrevNotificationsSync();

        assertEquals(1, notifs.size());
        PrevNotificationEntity pe = (PrevNotificationEntity) notifs.get(0);
        assertEquals(pe.getAppName(), prevNotification.getAppName());
        assertEquals(pe.getDate(), prevNotification.getDate());
        assertEquals(1, pe.getId());
        assertEquals(pe.getNotificationContext(), prevNotification.getNotificationContext());
    }

    @Test
    public void shouldDeletePrevNotification() {
        PrevNotificationEntity prevNotification = new PrevNotificationEntity();
        prevNotification.setAppName("app");
        prevNotificationDao.insert(prevNotification);
        List notifs = prevNotificationDao.loadAllPrevNotificationsSync();

        assertEquals(1, notifs.size());
        prevNotificationDao.deleteAll();
        notifs = prevNotificationDao.loadAllPrevNotificationsSync();
        assertEquals(0, notifs.size());
    }

    @Test
    public void shouldInsertMinNotification() {
        MinNotificationEntity minNotification = new MinNotificationEntity();
        minNotification.setAppName("app");
        minNotification.setDate(new Date());
        minNotification.setProfileName("prof");
        minNotification.setNotificationContext("this is the notification");
        minNotificationDao.insert(minNotification);
        List notifs = minNotificationDao.loadMinNotificationsFromProfileSync("prof");

        MinNotificationEntity pe = (MinNotificationEntity) notifs.get(0);
        assertEquals(pe.getAppName(), minNotification.getAppName());
        assertEquals(pe.getDate(), minNotification.getDate());
        assertEquals(1, pe.getId());
        assertEquals(pe.getNotificationContext(), minNotification.getNotificationContext());
    }

    @Test
    public void shouldDeleteMinNotification() {
        MinNotificationEntity minNotification = new MinNotificationEntity();
        minNotification.setAppName("app");
        minNotification.setProfileName("prof");
        minNotificationDao.insert(minNotification);
        List notifs = minNotificationDao.loadMinNotificationsFromProfileSync("prof");

        assertEquals(1, notifs.size());
        minNotificationDao.deleteAll();
        notifs = minNotificationDao.loadMinNotificationsFromProfileSync("prof");
        assertEquals(0, notifs.size());
    }

    @Test
    public void shouldInsertProfile() {
        ProfileEntity profile = new ProfileEntity();
        profile.active = false;
        profile.appsToBlock = new ArrayList<String>(Arrays.asList("alpha", "beta", "gamma"));
        profile.name = "testProfile";
        profileDao.insert(profile);
        List profiles = profileDao.loadAllProfilesAsync();

        assertEquals(1, profiles.size());
        ProfileEntity pe = (ProfileEntity) profiles.get(0);
        assertEquals(pe.getActive(), profile.getActive());
        assertEquals(pe.getAppsToBlock(), profile.getAppsToBlock());
        assertEquals(pe.getName(), profile.getName());
    }

    @Test
    public void shouldDeleteProfile() {
        ProfileEntity profile = new ProfileEntity();
        profile.name = "testProfile";
        profileDao.insert(profile);
        List profiles = profileDao.loadAllProfilesAsync();

        assertEquals(1, profiles.size());
        profileDao.deleteAll();
        profiles = profileDao.loadAllProfilesAsync();
        assertEquals(0, profiles.size());
    }


    @Test
    public void shouldInsertSchedule() {
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setActive(true);
        schedule.setDurationHr(1);
        schedule.setDurationMin(1);
        schedule.setIsEnabled(true);
        schedule.setName("sche");
        schedule.setRepeatWeekly(true);
        scheduleDao.insert(schedule);
        List schedules = scheduleDao.loadAllSchedulesSync();

        assertEquals(1, schedules.size());
        ScheduleEntity se =  (ScheduleEntity) schedules.get(0);
        assertEquals(schedule.getActive(), se.getActive());
        assertEquals(schedule.getDurationHr(),se.getDurationHr());
        assertEquals(schedule.getDurationMin(), se.getDurationMin());
        assertEquals(schedule.getIsEnabled(), se.getIsEnabled());
        assertEquals(schedule.getName(), se.getName());
        assertEquals(schedule.getRepeatWeekly(), se.getRepeatWeekly());

    }

    @Test
    public void shouldDeleteSchedule() {
        ScheduleEntity schedule = new ScheduleEntity();

        schedule.setName("sche");
        scheduleDao.insert(schedule);
        List schedules = scheduleDao.loadAllSchedulesSync();

        assertEquals(1, schedules.size());
        scheduleDao.deleteAll();
        schedules = scheduleDao.loadAllSchedulesSync();
        assertEquals(0, schedules.size());

    }
}
