package com.example.Project;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DAL.CRUD;
import com.example.DAL.GetSetters;

import java.util.ArrayList;

/**
 * Created by Joakim on 09/11/2016.
 */

public class ManageContacts {


    public boolean createOrUpdate(CRUD datasource, GetSetters task, View view, final Context context) {

        //Get all layouts in Listheader.xml
        final ArrayList<TextView> myEditTextList = new ArrayList<TextView>();
        final LinearLayout promtsViewLayout = (LinearLayout) view;
        for (int i = 0; i < promtsViewLayout.getChildCount(); i++) {
            if(promtsViewLayout.getChildAt(i) instanceof EditText) {
                myEditTextList.add((EditText) promtsViewLayout.getChildAt(i));
            }
        }

        final EditText phonenumber = (EditText) view
                .findViewById(R.id.text_field_three);
        String phonenumberValidation = phonenumber.getText().toString()
                .trim();
        final boolean phonenumberVal = phonenumber.getText().toString()
                .matches("[0-9]+$");


        if(phonenumberValidation.length() < 8 || !phonenumberVal) {
            Toast.makeText(context,
                    "Phone number must be longer than 8 numbers\nAnd consist of only Numbers", Toast.LENGTH_LONG)
                    .show();
        }

        if(phonenumberValidation.length() >= 8) {

            for (int i = 0; i <= 1; i++) {
                final boolean match = myEditTextList.get(i).getText().toString().trim().matches("[a-öA-Ö_]+$");

                if(!match) {
                    Toast.makeText(context, myEditTextList.get(i).getText() + " - " + myEditTextList.get(i).getHint() + " can only contain characters",
                            Toast.LENGTH_SHORT).show();
                } else if(match && i == 1) {
                    if(task == null) {
                        datasource.createTask(myEditTextList.get(0).getText().toString(), myEditTextList.get(1).getText().toString(), phonenumberValidation);
                    } else {
                        datasource.updateTask(task, myEditTextList.get(0).getText().toString(), myEditTextList.get(1).getText().toString(), phonenumberValidation);
                    }
                    return true;
                }
            }

            Toast.makeText(context, " Firstname updated to: "
                    + myEditTextList.get(0).getText().toString()
                    + "\n Lastname updated to: "
                    + myEditTextList.get(1).getText().toString()
                    + "\n Phone number updated to: "
                    + phonenumberValidation, Toast.LENGTH_LONG).show();
        }

        return false;
    }

    public void removeContact(CRUD datasource, GetSetters task, ArrayAdapter<GetSetters> listAdapter, final Context context) {
        datasource.deleteTask(task);
        listAdapter.remove(task);
        listAdapter.notifyDataSetChanged();
        Toast.makeText(context, " User Deleted ", Toast.LENGTH_LONG).show();
    }

}

