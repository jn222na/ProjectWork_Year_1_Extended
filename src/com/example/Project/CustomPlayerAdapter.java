package com.example.Project;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.DAL.GetSetters;

import java.util.List;

/**
 * Created by Jocke on 2016-11-18.
 */
class CustomPlayerAdapter extends ArrayAdapter<GetSetters> {

    private Context context;
    private int layoutResourceId;
    private List<GetSetters> data = null;

    CustomPlayerAdapter(Context context, int resource,List<GetSetters> data) {
        super(context, resource);
        this.context = context;
        this.layoutResourceId = resource;
        this.data = data;
    }

}
