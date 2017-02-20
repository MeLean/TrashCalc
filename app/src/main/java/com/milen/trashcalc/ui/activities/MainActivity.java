package com.milen.trashcalc.ui.activities;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.zxing.client.android.CaptureActivity;
import com.milen.trashcalc.R;
import com.milen.trashcalc.api.TrashEntity;
import com.milen.trashcalc.constants.Preferences;
import com.milen.trashcalc.ui.fragments.ClientsFragment;
import com.milen.trashcalc.utils.Validator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnLongClickListener {

    private static final int SCANNING_REQUEST_CODE = 1234;
    private static final String TAG = "AppDebug";

    private EditText mScannedText;
    private EditText mFullTruck;
    private EditText mEmptyTruck;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mScannedText = (EditText) findViewById(R.id.et_scanned_text);
        mScannedText.setOnLongClickListener(this);
        mScannedText.setFocusable(false);

        mFullTruck = (EditText) findViewById(R.id.et_entered_trash);
        mEmptyTruck = (EditText) findViewById(R.id.et_empty_trash);

        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String scannedText = mScannedText.getText().toString();
                String fullTruck = mFullTruck.getText().toString();
                String emptyTruck = mEmptyTruck.getText().toString();

                double fullTruckValue = 0.0;
                double emptyTruckValue = 0.0;
                boolean validTrash = false;

                if(!fullTruck.equals("") && !emptyTruck.equals("")) {
                    try {
                        fullTruckValue = Double.parseDouble(fullTruck);
                        emptyTruckValue = Double.parseDouble(emptyTruck);

                        validTrash = Validator.isValidateDiff(fullTruckValue, emptyTruckValue);
                    }catch (ParseException ex){
                       //not valid data is entered
                    }
                }


                if (Validator.isValidCode(scannedText) && validTrash) {
                    String dateAsString = (String) android.text.format.DateFormat.
                            format(Preferences.NODE_DATE_FORMAT_STRING, System.currentTimeMillis());

                    //todo check for internet and save values
                    DatabaseReference fireBaseDb = FirebaseDatabase.getInstance().getReference();
                    fireBaseDb.child(Preferences.FB_ENTITIES_TABLE)
                            .child(dateAsString + "_" + scannedText)
                            .setValue(
                                    new TrashEntity(
                                            ServerValue.TIMESTAMP,
                                            scannedText,
                                            fullTruckValue,
                                            emptyTruckValue
                                    )
                            );

                    //clear values
                    mScannedText.setText("");
                    mScannedText.setFocusable(false);
                    mFullTruck.setText("");
                    mEmptyTruck.setText("");

                    Snackbar.make(view, "Данните са запазени!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else {
                    Snackbar.make(view, "Въвелисте некоректни данни!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(getIntent().hasExtra("start_scanner")){
            startScannerActivity();
        }

    }

    private void startScannerActivity() {
        Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
        intent.setAction("com.google.zxing.client.android.SCAN");
        intent.putExtra("SAVE_HISTORY", true);
        startActivityForResult(intent, SCANNING_REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startScannerActivity();
        } else if (id == R.id.nav_exit) {
            finish();
            System.exit(0);
        } else if (id == R.id.nav_users) {
            FragmentManager fm = getSupportFragmentManager();
            ClientsFragment dialogFragment = new ClientsFragment();
            dialogFragment.show(fm, ClientsFragment.TAG);

        } else if (id == R.id.nav_get_report) {
            //todo checkFor internet
            startActivity(new Intent(MainActivity.this, ReportActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCANNING_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //todo checkFor internet
                String contents = data.getStringExtra("SCAN_RESULT");
                mScannedText.setText(contents);
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "RESULT_CANCELED");
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.et_scanned_text) {
            Toast.makeText(MainActivity.this, "Въведи ръчно кода!", Toast.LENGTH_SHORT).show();
            mScannedText.setFocusableInTouchMode(true);
        }
        return true;
    }
}
