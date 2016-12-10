package com.example.Project.fragments;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Jocke on 2016-12-09.
 */

public class NavigationDrawerFragment extends Fragment {

    public static String ARG_PLANET_NUMBER = "navigation_drawer_frag";

    public static NavigationDrawerFragment asd(int index) {
        NavigationDrawerFragment f = new NavigationDrawerFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }
    //getArguments().getInt("someInt", 0);
//

}
