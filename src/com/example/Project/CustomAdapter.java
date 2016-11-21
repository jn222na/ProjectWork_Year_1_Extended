package com.example.Project;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.DAL.GetSetters;

import java.util.List;


/**
 * Created by Joakim on 16/11/2016.
 */

class CustomListAdapter extends ArrayAdapter<GetSetters> {

    private Context context;
    private int layoutResourceId;
    private List<GetSetters> data = null;


    CustomListAdapter(Context context, int layoutResourceId, List<GetSetters> objects) {
        super(context, layoutResourceId, objects);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        GetSettersHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new GetSettersHolder();
            holder.firstname = (TextView) row.findViewById(R.id.firstname_ListItem);
            holder.lastname = (TextView) row.findViewById(R.id.lastname_ListItem);
//            holder.phonenumber = (TextView) row.findViewById(R.id.phonenumber_ListItem);

            row.setTag(holder);
        }

        holder = (GetSettersHolder) row.getTag();
        setHolder(position,holder);

        return row;
    }

    private void setHolder(int position, GetSettersHolder holder) {

        TableRow.LayoutParams tlparams = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT, 1.0f);

        GetSetters in = (GetSetters) data.get(position);
        setHolderDesign(in.getFirstname(), holder.firstname,tlparams);
        setHolderDesign(in.getLastname(), holder.lastname,tlparams);
        //TODO: Phonenumber?
    }
    private void setHolderDesign(String textOfField, TextView holderDesign, TableRow.LayoutParams tlparams){
        holderDesign.setText(textOfField);
        holderDesign.setLayoutParams(tlparams);
    }
//    private int convertPixelsToDp(int value) {
//        int NOSE_POSITION_DP = 150;
//        float scale = getContext().getResources().getDisplayMetrics().density;
//        return (int) (NOSE_POSITION_DP * scale + 0.5f);
//    }


    private static class GetSettersHolder {

        TextView firstname;
        TextView lastname;
        TextView phonenumber;
    }
}

