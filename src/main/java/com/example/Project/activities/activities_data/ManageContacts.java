package com.example.Project.activities.activities_data;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DAL.CRUD;
import com.example.DAL.GetSetters;
import com.example.Project.R;

import java.util.ArrayList;

/**
 * Created by Joakim on 09/11/2016.
 */

public class ManageContacts {

    private Context context;

    public ManageContacts(Context context) {
        this.context = context;
    }

    public boolean createOrUpdateContact(CRUD datasource, GetSetters task, View view) {

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
            showMessage("Phone number must be longer than 8 numbers\nAnd consist of only Numbers");
        }

        if(phonenumberValidation.length() >= 8) {

            for (int i = 0; i <= 1; i++) {
                final boolean match = myEditTextList.get(i).getText().toString().trim().matches("[a-öA-Ö]+$");
                String firstName = myEditTextList.get(0).getText().toString();
                String lastName = myEditTextList.get(1).getText().toString();

                if(!match) {
                    showMessage(myEditTextList.get(i).getText() + " - " + myEditTextList.get(i).getHint() + " can only contain characters");
                    return false;
                } else if(i == 1) {
                    if(task == null) {
                        datasource.createTask(firstName, lastName, phonenumberValidation);
                    } else {
                        datasource.updateTask(task, firstName, lastName, phonenumberValidation);
                    }

                    showMessage(" Firstname updated to: "
                            + firstName
                            + "\n Lastname updated to: "
                            + lastName
                            + "\n Phone number updated to: "
                            + phonenumberValidation);
                    return true;
                }
            }
        }

        return false;
    }

    public void removeContact(CRUD datasource, GetSetters task, ArrayAdapter<GetSetters> listAdapter) {
        datasource.deleteTask(task);
        listAdapter.remove(task);
        listAdapter.notifyDataSetChanged();
        showMessage(" User Deleted ");
    }

    private void showMessage(CharSequence text) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}

