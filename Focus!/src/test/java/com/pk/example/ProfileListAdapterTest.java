package com.pk.example;

import com.pk.example.entity.ProfileEntity;

//import org.testng.annotations.Test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;


/**
 * Created by kathe on 10/29/2017.
 */

public class ProfileListAdapterTest {

    @Test
    public void testCreation(){
        String [] profiles = new String [] {"test profile", "new profile", "profile12", "test ", "new 2018", "profile1229"};

        ArrayList<ProfileEntity> profileList = new ArrayList<ProfileEntity>();
        String [] strings = new String [] {"com.facebook.orca", "com.appname.fake", "com.something.else"};
        ArrayList<String> appsToBlock = new ArrayList<String>(Arrays.asList(strings));
        Profile profile = new Profile("name", appsToBlock, false);
        for(int i = 0; i < profiles.length; i++){
            ProfileEntity profileEntity = new ProfileEntity(profile);
            profileEntity.setName(profiles[i]);
        }

        ProfileListActivity test = mock(ProfileListActivity.class);

        ProfileListAdapter profileListAdapter = new ProfileListAdapter(test,
                R.layout.schedule_list_row, profileList);

        assertEquals(profileListAdapter.getCount(), profiles.length);
        for(int i = 0; i < profileList.size(); i++){
            assertEquals(profileList.get(i), profileListAdapter.getItem(i));
        }
    }
}
