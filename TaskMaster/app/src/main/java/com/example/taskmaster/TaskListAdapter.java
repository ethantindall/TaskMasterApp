package com.example.taskmaster;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Calendar;

import static com.example.taskmaster.MainActivity.tasks;

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    public int counter = 0;
    public TaskListAdapter() {}

    @Override
    public int getItemViewType(final int position) {
        return R.layout.frame_textview;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        //give each checkbox the id associated with the TaskObject
        holder.getCheckBox().setId(MainActivity.displayList.getObject(position).getId());
        //Give each textview the title and Id associated with the TaskObject
        holder.getView().setText(MainActivity.displayList.getObject(position).getTitle());
        holder.getView().setId(MainActivity.displayList.getObject(position).getId());


        holder.getTrash().setId(MainActivity.displayList.getObject(position).getId());

        //If the task is completed, go ahead and set the checkbox and strikethrough the text.
        if (MainActivity.displayList.getObject(position).isDone()) {
            holder.getView().setPaintFlags(holder.getView().getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.getCheckBox().setChecked(true);
        }
        else {
            holder.getView().setPaintFlags(0);
            holder.getCheckBox().setChecked(false);
        }

        //holder.getView().setText(MainActivity.displayList.getObject(position));
        holder.getDueByView().setText("Due: " + (MainActivity.displayList.getObject(position).getDueBy().get(Calendar.MONTH)+1) + "/" + MainActivity.displayList.getObject(position).getDueBy().get(Calendar.DATE) + "/" + MainActivity.displayList.getObject(position).getDueBy().get(Calendar.YEAR));
        counter++;
    }



    //how many items the recycler view should display
    @Override
    public int getItemCount() {
        if (MainActivity.displayList.equals("0")) {
            return 0;
        } else {
            return MainActivity.displayList.getSize();
        }
    }


}