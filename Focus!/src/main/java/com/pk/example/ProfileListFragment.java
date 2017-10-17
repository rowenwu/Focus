package com.pk.example;

import android.app.ListFragment;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pk.example.dao.ProfileDao;

/**
 * Created by williamxu on 10/16/17.
 */

public class ProfileListFragment extends ListFragment {

    private ProfileDao profileDao;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile_list, container, false);

        // Create ArrayAdapter object to wrap the data source

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),R.layout.fragment_profile_list, profileDao.loadAllProfileNamesAsync());
        // Bind adapter to the ListFragment
        setListAdapter(adapter);
        //  Retain the ListFragment instance across Activity re-creation
        setRetainInstance(true);
        return rootView;
    }

    // Handle Item click event
    public void onListItemClick(ListView l, View view, int position, long id) {
        //TODO: take me to the profile
    }


}
