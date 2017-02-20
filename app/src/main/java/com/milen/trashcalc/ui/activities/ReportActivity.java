package com.milen.trashcalc.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.milen.trashcalc.R;
import com.milen.trashcalc.api.TrashEntity;
import com.milen.trashcalc.constants.Preferences;
import com.milen.trashcalc.ui.adapters.firebase_adapters.CustomFireBaseRecyclerAdapter;
import com.milen.trashcalc.ui.adapters.firebase_adapters.ReportHolder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    private DatabaseReference mDbReference;
    private ArrayList<TrashEntity> mTrashEntities;
    private CustomFireBaseRecyclerAdapter mMessagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabReport);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail(view);
            }
        });

        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }



        mDbReference = FirebaseDatabase.getInstance().getReference();
        double startDate  = System.currentTimeMillis() - 2629746000d; //one month in milliseconds is 2629746000
        mMessagesAdapter = new CustomFireBaseRecyclerAdapter(
                this,
                TrashEntity.class,
                R.layout.report_item,
                ReportHolder.class,
                FirebaseDatabase.getInstance().getReference()
                        .child(Preferences.FB_ENTITIES_TABLE)
                        .orderByChild(Preferences.FB_CHILD_DATE)
                        .startAt(startDate)

                        //.endAt(startDate + 2629746000d) //todo fix this
        );

        RecyclerView reportsRecycler = (RecyclerView) findViewById(R.id.report_recycler);
        reportsRecycler.setLayoutManager(new LinearLayoutManager(this));
        reportsRecycler.setAdapter(mMessagesAdapter);
    }

    private void sendEmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_SUBJECT, Preferences.EMAIL_SUBJECT);
        intent.putExtra(Intent.EXTRA_TEXT, makeEmailText() );


        PackageManager manager = getPackageManager();
        String message = "Нямате приложение, което може да изпраща електронна поща!";
        if (intent.resolveActivity(manager) != null) {
            startActivityForResult(intent, 0);
        } else {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }
    }

    private String makeEmailText() {
        String result = Preferences.NO_ENTITIES;
        double totalWeight = 0;
        int itemsCount = mMessagesAdapter.getItemCount();
        if(itemsCount > 0){
            result = "";

            for (int i=0; i<itemsCount; i++){
                TrashEntity entity = mMessagesAdapter.getItem(i);
                double entityWeight = entity.getTrashWeight();
                totalWeight += entityWeight;
                result += String.format(Locale.getDefault(),
                        "%1$s%2$s %3$s%4$s - %5$s%6$s\n",
                        Preferences.DATE_PREFIX,
                        android.text.format.DateFormat.format(
                                Preferences.EMAIL_DATE_FORMAT_STRING, entity.getDateAsLong()
                        ),
                        Preferences.TRUCK_ID_PREFIX,
                        entity.getTruckId(),
                        entityWeight,
                        Preferences.WEIGHT_SUFFIX
                );
            }
            DecimalFormat df = new DecimalFormat(Preferences.DECIMAL_FORMAT_STRING);
            result += Preferences.WEIGHT_TOTAL + df.format(totalWeight);
        }

        return result;
    }
}
