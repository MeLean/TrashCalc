package com.milen.trashcalc.ui.adapters.firebase_adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.milen.trashcalc.R;

public class ReportHolder  extends RecyclerView.ViewHolder{
    private TextView entityCountNum;
    private TextView entityDate;
    private TextView entityTruckId;
    private TextView entityTrashWeight;


    public ReportHolder(View itemView) {
        super(itemView);
        entityCountNum = (TextView)itemView.findViewById(R.id.entity_count_num);
        entityDate = (TextView)itemView.findViewById(R.id.entity_date);
        entityTruckId = (TextView)itemView.findViewById(R.id.entity_truck_id);
        entityTrashWeight = (TextView)itemView.findViewById(R.id.entity_trash_weight);
    }

    public TextView getEntityCountNum() {
        return entityCountNum;
    }

    public void setEntityCountNum(TextView entityCountNum) {
        this.entityCountNum = entityCountNum;
    }

    public TextView getEntityDate() {
        return entityDate;
    }

    public void setEntityDate(TextView entityDate) {
        this.entityDate = entityDate;
    }

    public TextView getEntityTruckId() {
        return entityTruckId;
    }

    public void setEntityTruckId(TextView entityTruckId) {
        this.entityTruckId = entityTruckId;
    }

    public TextView getEntityTrashWeight() {
        return entityTrashWeight;
    }

    public void setEntityTrashWeight(TextView entityTrashWeight) {
        this.entityTrashWeight = entityTrashWeight;
    }
}
