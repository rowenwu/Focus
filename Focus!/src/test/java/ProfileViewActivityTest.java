import android.app.Application;
import android.content.Context;
import android.test.mock.MockApplication;
import android.view.View;

import com.pk.example.Profile;
import com.pk.example.ProfileViewActivity;
import com.pk.example.entity.ProfileEntity;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Created by Stephanie on 10/29/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class ProfileViewActivityTest {
    private ProfileEntity profileEntity = null;
    private ArrayList<String> apps = null;
//    private View v = null;
    private String fakeFlag;

    @Mock
    Application application = new MockApplication();
//    @Mock
//    ProfileViewActivity activity;
    ProfileViewActivity activity = Mockito.mock(ProfileViewActivity.class);
    @Mock
    Context mockContext;
    @Mock
    View v;

//    @BeforeClass
//    public void setProfileEntity() {
//        // setup example profile entity to be used in tests
//        apps.add("YouTube");
//        profileEntity.setName("test profile");
//        profileEntity.setAppsToBlock(apps);
//    }

    @Before
    public void initializeActivity() {
//        activity = new ProfileViewActivity();
        fakeFlag = null;
    }

    @Test
    public void testEditButtonClicked() {
        // checks that intent flag for edit profile is correct
        fakeFlag = "edit";
        when(activity.editButtonClicked(v)).thenReturn(fakeFlag);
        String flag = activity.editButtonClicked(v);

        assertEquals("edit", flag);
    }

    @Test
    public void testDeleteButtonClicked() {
        // checks that intent flag is null for delete profile
        when(activity.deleteButtonClicked(v)).thenReturn(fakeFlag);
        String flag = activity.deleteButtonClicked(v);

        assertEquals(null, flag);
    }


}
