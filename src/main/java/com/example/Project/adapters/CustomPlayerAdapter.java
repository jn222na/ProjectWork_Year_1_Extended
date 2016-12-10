package com.example.Project.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.DAL.GetSetters;
import com.example.Project.R;

import java.util.List;


/**
 * Created by Joakim on 16/11/2016.
 */

public class CustomPlayerAdapter extends ArrayAdapter<GetSetters> {

    private final Context context;
    private int layoutResourceId;
    private List<GetSetters> data = null;

    private int x = 3;

    public CustomPlayerAdapter(Context context, int layoutResourceId, List<GetSetters> objects) {
        super(context, layoutResourceId, objects);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = objects;
    }

    @NonNull
    @Override //don't override if you don't want the default spinner to be a two line view
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return initView(position, convertView);
    }

    private View initView(int position, View convertView) {
        if(convertView == null)
            convertView = View.inflate(getContext(),
                    layoutResourceId,
                    null);
        TextView firstname = (TextView) convertView.findViewById(R.id.firstname_spinnerItem);
        TextView lastname = (TextView) convertView.findViewById(R.id.lastname_spinnerItem);
        firstname.setText(getItem(position).getFirstname() + "  ");

        String stripLastname = getItem(position).getLastname();
//        stripLastname = stripLastname.substring(0, 1);
        lastname.setText(stripLastname);
        return convertView;
    }


}

