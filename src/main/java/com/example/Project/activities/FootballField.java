
package com.example.Project.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.DAL.GetSetters;
import com.example.Project.R;
import com.example.Project.adapters.CustomPlayerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;


//TODO: Give the user the ability to name the screenshot, this will allow him to go back and see how the formation was before.

public class FootballField extends FragmentActivity {


    private ArrayList<GetSetters> values;
    private View allChildren;
    private CustomPlayerAdapter playerAdapter;
    private String fname;
    private String stripLastname;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_field);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        Bundle bundle = getIntent().getExtras();
        values = bundle.getParcelableArrayList("Values");
        playerAdapter = new CustomPlayerAdapter(getBaseContext(), R.layout.playeradaperview, values);
        sortPlayerAdapter();
        setPlayers();
        setupDrawer();
    }

    @SuppressLint("NewApi")
    private void setupDrawer() {

        // Getting reference to the DrawerLayout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nvView);
        navigationView.bringToFront();
        drawerLayout.requestLayout();
        setupDrawerContent(navigationView);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.send_screenShot:
                                //Close drawer and wait until its closed
                                drawerLayout.closeDrawer(GravityCompat.START);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sendMsg();
                                    }
                                }, 300);
                                break;

                        }

                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.send_screenShot:
                Log.d("DSA", "selectDrawerItem: ");
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }


    private void sortPlayerAdapter() {
        playerAdapter.sort(new Comparator<GetSetters>() {
            public int compare(GetSetters arg0, GetSetters arg1) {
                return arg0.getFirstname().compareTo(arg1.getFirstname());
            }
        });
    }

    private void setPlayers() {
        //Get all layouts in Listheader.xml
        final ArrayList<Button> allBtnsInFootballXml = new ArrayList<Button>();
        final RelativeLayout footballView = (RelativeLayout) findViewById(R.id.footballFieldView);

        for (int i = 0; i < footballView.getChildCount(); i++) {
            if(footballView.getChildAt(i) instanceof Button) {
                allBtnsInFootballXml.add((Button) footballView.getChildAt(i));
                allChildren = footballView.getChildAt(i);
                allChildren.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = dialogFootballField(v);
                        TextView title = new TextView(getApplicationContext());
                        title.setText(R.string.alertDialogTitle);
                        title.setGravity(Gravity.CENTER);
                        title.setTextSize(30);
                        title.setBackgroundColor(Color.GRAY);
                        title.setTextColor(Color.WHITE);
                        dialog.setCustomTitle(title);
                        dialog.show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
                    }
                });
            }
        }
    }


    private AlertDialog dialogFootballField(final View v) {

        RelativeLayout footballFieldView = (RelativeLayout) findViewById(R.id.footballFieldView);
        final Button mButton = (Button) v;
        //Initialize the Alert Dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setAdapter(playerAdapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                fname = values.get(which).getFirstname();
                stripLastname = values.get(which).getLastname();
                stripLastname = stripLastname.substring(0, 1);
                mButton.setText(fname + " " + stripLastname);
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        return builder.create();
    }

    public void sendMsg() {
//        Date now = new Date();
//        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        String screenshot ="shot";
        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + screenshot + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra("sms_body", "Formation");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
        intent.setType("image/gif");
        startActivity(Intent.createChooser(intent, "Send"));
    }

}
