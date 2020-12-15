package com.example.taskmaster;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView view;
    private TextView dueByView;
    private CheckBox checkBox;
    private Button buttonView;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.titleText);
        dueByView = itemView.findViewById(R.id.dueByDate);
        checkBox = itemView.findViewById(R.id.checkBox);
        buttonView = itemView.findViewById(R.id.trash);

    }
    //get each required view
    public TextView getView(){
        return view;
    }
    public TextView getDueByView() {
        return dueByView;
    }
    public CheckBox getCheckBox() {return checkBox;}
    public Button getTrash() {return buttonView;}

}