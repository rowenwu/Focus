package com.pk.example;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pk.example.entity.ScheduleEntity;

import java.util.List;

public class SchedulesViewAdapter extends RecyclerView.Adapter<SchedulesViewAdapter.RecyclerViewHolder> {

    private List<ScheduleEntity> scheduleEntityList;
    private View.OnLongClickListener longClickListener;

    public SchedulesViewAdapter(List<ScheduleEntity> scheduleEntityList, View.OnLongClickListener longClickListener) {
        this.scheduleEntityList = scheduleEntityList;
        this.longClickListener = longClickListener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        ScheduleEntity scheduleEntity = scheduleEntityList.get(position);
        holder.itemTextView.setText(scheduleEntity.getName());
//        holder.nameTextView.setText(scheduleEntity.getPersonName());
//        holder.dateTextView.setText(scheduleEntity.getBorrowDate().toLocaleString().substring(0, 11));
        holder.itemView.setTag(scheduleEntity);
        holder.itemView.setOnLongClickListener(longClickListener);
    }

    @Override
    public int getItemCount() {
        return scheduleEntityList.size();
    }

    public void addItems(List<ScheduleEntity> scheduleEntityList) {
        this.scheduleEntityList = scheduleEntityList;
        notifyDataSetChanged();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTextView;
        private TextView nameTextView;
        private TextView dateTextView;

        RecyclerViewHolder(View view) {
            super(view);
            itemTextView = (TextView) view.findViewById(R.id.itemTextView);
//            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
//            dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        }
    }
}
