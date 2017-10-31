package com.pk.example;

import android.app.Application;
import android.content.Context;
import android.support.v4.widget.TextViewCompat;
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

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProfileViewActivityTest {
    private String fakeFlag;

    ProfileViewActivity activity = Mockito.mock(ProfileViewActivity.class);

    @Mock
    View v;

    @Before
    public void initializeActivity() {
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

    @Test
    public void testCreateButtonClicked() {
        // checks that intent flag is null for create profile
        when(activity.createButtonClicked(v)).thenReturn(fakeFlag);
        String flag = activity.createButtonClicked(v);

        assertEquals(null, flag);
    }

    @Test
    public void testSaveButtonClicked() {
        when(activity.saveButtonClicked(v)).thenReturn(fakeFlag);
        String flag = activity.createButtonClicked(v);

        assertEquals(null, flag);
    }

}
