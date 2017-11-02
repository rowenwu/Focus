//
//import android.content.Intent;
//import android.os.IBinder;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.core.deps.guava.collect.Collections2;
//import android.support.test.filters.MediumTest;
//import android.support.test.rule.ServiceTestRule;
//import android.support.test.runner.AndroidJUnit4;
//
//import com.pk.example.NotificationAdapter.NLService;
//import com.pk.example.Profile;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.concurrent.TimeoutException;
//
//import static org.hamcrest.CoreMatchers.any;
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertThat;
//import static org.junit.Assert.*;
//
//
///**
// * JUnit4 test that uses a {@link ServiceTestRule} to interact with a bound service.
// * <p>
// * {@link ServiceTestRule} is a JUnit rule that provides a
// * simplified mechanism to start and shutdown your service before
// * and after the duration of your test. It also guarantees that the service is successfully
// * connected when starting (or binding to) a service. The service can be started
// * (or bound) using one of the helper methods. It will automatically be stopped (or unbound) after
// * the test completes and any methods annotated with
// * <a href="http://junit.sourceforge.net/javadoc/org/junit/After.html"><code>After</code></a> are
// * finished.
// * <p>
// * Note: This rule doesn't support {@link android.app.IntentService} because it's automatically
// * destroyed when {@link android.app.IntentService#onHandleIntent(android.content.Intent)} finishes
// * all outstanding commands. So there is no guarantee to establish a successful connection
// * in a timely manner.
// */
//@MediumTest
//@RunWith(AndroidJUnit4.class)
//public class NLServiceTest {
//    @Rule
//    public final ServiceTestRule mServiceRule = new ServiceTestRule();
//
//
//    @Test
//    public void testWithBoundService() throws TimeoutException {
//        // Create the service Intent.
//        Intent serviceIntent =
//                new Intent(InstrumentationRegistry.getTargetContext(), NLService.class);
//
//        // Data can be passed to the service via the Intent.
//        serviceIntent.putExtra(NLService.SEED_KEY, 42L);
//
//        // Bind the service and grab a reference to the binder.
//        IBinder binder = mServiceRule.bindService(serviceIntent);
//
//        // Get the reference to the service, or you can call public methods on the binder directly.
//        NLService service = ((NLService.LocalBinder) binder).getService();
//
//        // Verify that the service is working correctly.
//        assertThat(service.getRandomInt(), is(any(Integer.class)));
//
//        service.resetBlockedApps();
//
//        // Test addProfile
//        String profileName = "test profile";
//        String [] strings = new String [] {"com.facebook.orca", "com.appname.fake", "com.something.else"};
//        ArrayList<String> appsToBlock = new ArrayList<String>(Arrays.asList(strings));
//        service.addProfile(profileName, appsToBlock);
//        service.addProfile(profileName, appsToBlock);
//
//
//
//        // Test getBlockedApps
//        ArrayList<String> blockedApps = service.getBlockedApps();
//
//        Collections.sort(appsToBlock);
//        Collections.sort(blockedApps);
//
//        assertEquals(appsToBlock, blockedApps);
//        assertEquals(profileName, service.getProfilesBlockingApp(strings[0]).get(0));
//
//        assertTrue(service.isAppBlocked(strings[1]));
//        assertTrue(service.isAppBlocked("com.something.else"));
//        assertTrue(service.isAppBlocked("com.facebook.orca"));
//
//
//        // Test addBlockedApp
//        String [] apps = new String [] {"com.facebook.orca", "com.newapp.name"};
//        ArrayList<String> newAppsToBlock = new ArrayList<String>(Arrays.asList(apps));
//        service.addBlockedApp("com.newapp.name", "new profile");
//        service.addBlockedApp("com.facebook.orca", "new profile");
//        appsToBlock.add("com.newapp.name");
//
//        blockedApps = service.getBlockedApps();
//        Collections.sort(appsToBlock);
//        Collections.sort(blockedApps);
//        assertEquals(appsToBlock, blockedApps);
//
//
//        // Test getProfileBlockingApp
//        String [] profiles = new String [] {"test profile", "new profile"};
//        ArrayList<String> profilesList = new ArrayList<String>(Arrays.asList(profiles));
//        ArrayList<String> getProfs = service.getProfilesBlockingApp("com.facebook.orca");
//        Collections.sort(getProfs);
//        Collections.sort(profilesList);
//        String prof = getProfs.get(0);
//        assertEquals(profilesList, getProfs);
//        assertTrue(service.isAppBlocked("com.newapp.name"));
//
//
//
//
//        // Test RemoveProfile
//        service.removeProfile("test profile", appsToBlock);
//        profilesList.remove(0);
//        assertEquals(profilesList, service.getProfilesBlockingApp("com.facebook.orca"));
//        assertFalse(service.isAppBlocked("com.appname.fake"));
//        assertFalse(service.isAppBlocked("com.something.else"));
//        assertFalse(service.isAppBlocked("com.facebook.orca"));
//        assertEquals(newAppsToBlock, service.getBlockedApps());
//
//
//    }
//}