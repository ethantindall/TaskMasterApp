package com.example.taskmaster;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class TaskList {
    private ArrayList<TaskObject> list = new ArrayList<>();

    //sort the tasklist by due date
    public void sortList(){
        Collections.sort(list, (o1, o2) -> {
            //ascending sort
            if (o1.getDueBy().get(Calendar.YEAR) != o2.getDueBy().get(Calendar.YEAR))
                return o1.getDueBy().get(Calendar.YEAR) - o2.getDueBy().get(Calendar.YEAR);


            else if (o1.getDueBy().get(Calendar.MONTH) != o2.getDueBy().get(Calendar.MONTH))
                return o1.getDueBy().get(Calendar.MONTH) - o2.getDueBy().get(Calendar.MONTH);


            else
                return o1.getDueBy().get(Calendar.DATE) - o2.getDueBy().get(Calendar.DATE);
        });
    }

    //remove task from list
   public void removeObject(int indexOf) {
        list.remove(indexOf);
        //completedAllTasksReward();
    }
    //get size of list
    public int getSize() {
        return list.size();
    }

    //add object to list
    public void addObject(TaskObject newTaskObject) {
        list.add(newTaskObject);
        sortList();
    }
    //get object in list
    public TaskObject getObject(int indexOf) {
        return (TaskObject) list.toArray()[indexOf];
    }
}
