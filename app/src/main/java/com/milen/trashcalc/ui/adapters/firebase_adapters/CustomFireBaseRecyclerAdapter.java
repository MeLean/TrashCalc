package com.milen.trashcalc.ui.adapters.firebase_adapters;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.Query;
import com.milen.trashcalc.R;
import com.milen.trashcalc.api.TrashEntity;
import com.milen.trashcalc.constants.Preferences;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomFireBaseRecyclerAdapter extends FirebaseRecyclerAdapter<TrashEntity, ReportHolder> {
    private static final String SHOW_DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";

    private AppCompatActivity mActivity;
    private ProgressDialog mProgressbar;

    public CustomFireBaseRecyclerAdapter(
            AppCompatActivity activity,
            Class<TrashEntity> modelClass,
            int modelLayout,
            Class<ReportHolder> viewHolderClass,
            Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);

        this.mActivity = activity;

        this.mProgressbar = new ProgressDialog(mActivity);
        this.mProgressbar.setTitle(mActivity.getString(R.string.loading_screen_title));
        this.mProgressbar.setMessage(mActivity.getString(R.string.loading_screen_text));
        this.mProgressbar.setIcon(R.mipmap.ic_launcher);
        mProgressbar.show();
    }


    @Override
    protected void populateViewHolder(ReportHolder viewHolder, TrashEntity model, int position) {
        if (model != null) {
            viewHolder.getEntityCountNum().setText(String.valueOf(position + 1));
            SimpleDateFormat formatter = new SimpleDateFormat(SHOW_DATE_FORMAT, Locale.getDefault());

            String formattedDate = Preferences.DATE_PREFIX  +
                    formatter.format(new Date(model.getDateAsLong()));
            viewHolder.getEntityDate().setText(formattedDate);

            String truckIdValue = Preferences.TRUCK_ID_PREFIX + model.getTruckId();
            viewHolder.getEntityTruckId().setText(String.valueOf(truckIdValue));
            DecimalFormat decimalFormat = new DecimalFormat(Preferences.DECIMAL_FORMAT_STRING);
            String trashWeightValue = String.format(Locale.getDefault()," %s%s",
                    decimalFormat.format(model.getTrashWeight()), Preferences.WEIGHT_SUFFIX);
            viewHolder.getEntityTrashWeight().setText(String.valueOf(trashWeightValue));
        }

        if (mProgressbar.isShowing()) {
            mProgressbar.dismiss();
        }
    }
}