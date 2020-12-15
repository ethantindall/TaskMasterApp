package com.example.taskmaster;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TaskObject {
    private int id;
    private String title;
    private String description;
    private boolean isDone;
    private boolean notify;
    private boolean addTask;
    private Calendar dueBy = new GregorianCalendar();
    private Date remindBy;
    private boolean repeat;
    private Date repeatEvery;

    public TaskObject(String title, int id, boolean repeat, boolean notify) {
        this.title = title;
        this.id = id;
        this.repeat = repeat;
        this.notify = notify;

    }
    //getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Calendar getDueBy() {
        return dueBy;
    }
    
    public long getDueByLong()
    {
        return dueBy.getTimeInMillis();
    }

    public void setDueBy(int month, int date, int year) {
        this.dueBy.set(Calendar.MONTH, month);
        this.dueBy.set(Calendar.DATE, date);
        this.dueBy.set(Calendar.YEAR, year);

    }

    public Date getRemindBy() {
        return remindBy;
    }

    public void setRemindBy(Date remindBy) {
        this.remindBy = remindBy;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public boolean isAddTask() {
        return addTask;
    }

    public void setAddTask(boolean addTask) {
        this.addTask = addTask;
    }

    public Date getRepeatEvery() {
        return repeatEvery;
    }

    public void setRepeatEvery(Date repeatEvery) {
        this.repeatEvery = repeatEvery;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
