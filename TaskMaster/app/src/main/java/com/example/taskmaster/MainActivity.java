package com.example.taskmaster;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.lang.String.valueOf;
import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;

public class MainActivity extends AppCompatActivity {
    public static TaskList tasks;
    public static Date sortByDate;
    public static Calendar calendar = new GregorianCalendar();
    MediaPlayer mp;
    public int taskIdCounter = 0;
    public TaskList dayViewTasks = new TaskList();
    public TaskList weekViewTasks = new TaskList();
    public TaskList monthViewTasks = new TaskList();
    public static TaskList displayList = new TaskList();
    public Calendar today = Calendar.getInstance();
    public int day = today.get(Calendar.DAY_OF_YEAR);
    public int week = today.get(Calendar.DAY_OF_YEAR)+7;
    public int month = today.get(Calendar.DAY_OF_YEAR)+30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get nav menu input
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DayViewFragment()).commit();

        //create the list where all the tasks are stored
        tasks = new TaskList();

        //if there is a previous save file, use that.
        File f = new File("/data/data/com.example.taskmaster/shared_prefs/shared preferences.xml");
        if (f.exists()){
            loadData();
        }
        //displaylist function creates the different day, week and month views
        buildDisplayList();
        displayList = dayViewTasks;
    }

    //Listens to see if any nav menu buttons are clicked
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        //if new task button
                        case R.id.nav_new:
                            selectedFragment = new NewTaskFragment();
                            break;
                        //if day view button
                        case R.id.nav_day:
                            selectedFragment = new DayViewFragment();
                            displayList = dayViewTasks;
                            break;
                        //if week view button
                        case R.id.nav_week:
                            selectedFragment = new WeekViewFragment();
                            displayList = weekViewTasks;
                            break;
                        //if month view button
                        case R.id.nav_month:
                            selectedFragment = new MonthViewFragment();
                            displayList = monthViewTasks;
                            break;
                    }
                    //set the selected fragment to the current one
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    //run if a checkbox is clicked by the user
    public void itemChecked(View view) {
        //get the required views
        CheckBox checkBox = (CheckBox) view;
        int num = checkBox.getId();
        TextView t = (TextView) findViewById(num);

        //if the checkbox is checked, find the TaskObject with the associated Id, mark it as done.
        if (checkBox.isChecked()) {
            playSound(view);
            for (int i = 0; i < tasks.getSize(); i ++) {
                if (tasks.getObject(i).getId() == num) {
                    //mark the task done
                    tasks.getObject(i).setDone(true);
                    //strikethrough the text
                    t.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    //Log.d("Alert", tasks.getObject(i).getTitle() + " is now set to " + tasks.getObject(i).isDone());
                    //play sound
                }
            }
            //otherwise, mark it not done and remove the strikethrough
        } else {
            for (int i = 0; i < tasks.getSize(); i ++) {
                if (tasks.getObject(i).getId() == num) {
                    //set to false
                    tasks.getObject(i).setDone(false);
                    //remove strikethrough
                    t.setPaintFlags(0);
                }
            }
        }
        saveData();
    }

    //play sound
    public void playSound(View view) {
        mp = MediaPlayer.create(this, R.raw.soundeffect1);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    //if delete button is selected
    public void clickTrash(View view) {
        Button button = (Button) view;
        int num = button.getId();
        for (int i = 0; i < tasks.getSize(); i ++) {
            if (tasks.getObject(i).getId() == num) {
                tasks.removeObject(i);
                buildDisplayList();

                saveData();

                //reload app with the saved data
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        }
    }

    //save the task data to shared preferences
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tasks);
        editor.putString("task list", json);
        editor.apply();
    }

    //load the app data from shared preferences
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);

        tasks = gson.fromJson(json, TaskList.class);

    }

    //For the new task page, when button clicked, get and save data
    public void addNewTask(View view) {
        //Gather all the user entered data
        Switch repeat = (Switch)findViewById(R.id.switch3);
        Switch notify = (Switch)findViewById(R.id.switch2);
        boolean repeatValue = repeat.isChecked();
        boolean notifyValue = notify.isChecked();

        EditText title = (EditText)findViewById(R.id.TaskName);
        String titleValue = title.getText().toString();

        EditText day = (EditText)findViewById(R.id.dayedit);
        EditText month = (EditText)findViewById(R.id.monthedit);
        EditText year = (EditText)findViewById(R.id.yearedit);

        String d = day.getText().toString();
        String m = month.getText().toString();
        String y = year.getText().toString();

        //if a string is empty, let the user know to fill it in
        if (!d.isEmpty() && !m.isEmpty() && !y.isEmpty() && !titleValue.isEmpty()) {
            int dnum = Integer.parseInt(d);
            int mnum = Integer.parseInt(m);
            int ynum = Integer.parseInt(y);
            Log.d("Alert", mnum + "/" + dnum + "/" + ynum);

            //Calendar dueBy;
            //Create Object and add it to list
            TaskObject taskObject = new TaskObject(titleValue, maximum(tasks), repeatValue, notifyValue);
            taskObject.setDueBy(mnum - 1, dnum, ynum);

            tasks.addObject(taskObject);

            //repeat the task once if the user desires
            if (repeatValue == true) {
                TaskObject taskObject2 = new TaskObject(titleValue, maximum(tasks), repeatValue, notifyValue);
                taskObject2.setDueBy(mnum - 1, dnum + 7, ynum);
                tasks.addObject(taskObject2);
            }
            taskIdCounter += 1;

            buildDisplayList();
            displayList = dayViewTasks;
            saveData();
            //When task is added, show toast and clear out entries
            Toast.makeText(getApplicationContext(),"Task Added!",Toast.LENGTH_SHORT).show();
            title.setText("");
            day.setText("");
            month.setText("");
            year.setText("");

        }
        else {
            Toast.makeText(getApplicationContext(),"Please enter all fields",Toast.LENGTH_SHORT).show();
        }
    }

    //find the highest task object id so that each one has a unique number
    public static int maximum(TaskList x) {
        int maxSoFar = 0;
        if(x.getSize() == 0) {return 0;}
        for (int i = 0; i < x.getSize(); i++) {
            if (x.getObject(i).getId() > maxSoFar) {
                maxSoFar = x.getObject(i).getId();
            }
        }
        return maxSoFar+1;
    }

    //create an unique display list holding the tasks due for that view
    private void buildDisplayList() {
        dayViewTasks = new TaskList();
        weekViewTasks = new TaskList();
        monthViewTasks = new TaskList();
        for (int i=0; i < tasks.getSize(); i++) {
            Calendar nn = tasks.getObject(i).getDueBy();
            if (nn.get(Calendar.DAY_OF_YEAR) == day) {
                dayViewTasks.addObject(tasks.getObject(i));
            } else if (nn.get(Calendar.DAY_OF_YEAR) > day && nn.get(Calendar.DAY_OF_YEAR) <= week)  {
                weekViewTasks.addObject(tasks.getObject(i));
            } else {
                monthViewTasks.addObject(tasks.getObject(i));
            }
        }
    }
}

