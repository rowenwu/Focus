package com.pk.example.clientui;

import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Stephanie on 11/13/2017.
 */

public class ProfileSorter {
    private List<ProfileEntity> profiles; // param passed in from ProfileListActivity
    private PriorityQueue<ProfileFrequency> profileFrequencies; // stores ProfileFrequency for all profiles in order
    private List<ProfileEntity> sortedProfiles; // stores ProfileEntity objects in same order as profileFrequencies
    private List<ScheduleEntity> schedules;

    public ProfileSorter(List<ProfileEntity> profiles, List<ScheduleEntity> schedules) {
        this.profiles = profiles;
        this.schedules = schedules;
        profileFrequencies = new PriorityQueue<>(profiles.size(), new Comparator<ProfileFrequency>() {
            public int compare(ProfileFrequency p1, ProfileFrequency p2) {
                return Integer.compare(p1.getFrequency(), p2.getFrequency());
            }
        });
        sortedProfiles = new ArrayList<>();
    }

    public List<ProfileEntity> getSortedProfiles() {
        sortProfiles();
        return sortedProfiles;
    }

    private void sortProfiles() {
        // create ProfileFrequency for each profile and add to priority queue
        for (ProfileEntity p : profiles) {
            profileFrequencies.add(new ProfileFrequency(p, schedules));
        }
        // get sorted profiles from priority queue
//        for (ProfileFrequency p : profileFrequencies) {
//            sortedProfiles.add(p.getProfileEntity());
//        }
        ProfileFrequency pf;
        while ((pf = profileFrequencies.poll()) != null ) {
            sortedProfiles.add(pf.getProfileEntity());
        }
    }
}

class ProfileFrequency {
    private ProfileEntity profile;
    private int frequency;
    private List<ScheduleEntity> schedules;

    public ProfileFrequency(ProfileEntity profile, List<ScheduleEntity> schedules) {
        this.profile = profile;
        frequency = 0;
        this.schedules = schedules;
        calculateFrequency();
    }

    public ProfileEntity getProfileEntity() {
        return profile;
    }

    public int getFrequency() {
        return frequency;
    }

    // count total number of times this profile appears in all schedules
    private void calculateFrequency() {
        for (ScheduleEntity s : schedules) {
            if (isInSchedule(s)) {
                frequency++;
            }
        }
    }

    private Boolean isInSchedule(ScheduleEntity schedule) {
        return schedule.getProfiles().contains(profile.getName());
    }
}