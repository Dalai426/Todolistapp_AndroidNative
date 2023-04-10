package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todoapp.model.Tasks;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Database.DatabaseHAndler;

public class MainActivity extends AppCompatActivity {


    private static final int JOB_ID = 1;
    RecyclerView recycler;
    Button btn;

    RecyclerAdapter adapter;

    LinkedList<Tasks>list;

    DatabaseHAndler db;


    SharedPreferences setting;
    public JobScheduler mScheduler;
    int mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler=findViewById(R.id.recycler);
        btn=findViewById(R.id.plus);

        setting=getSharedPreferences("todo", Context.MODE_PRIVATE);

        if(setting.getInt("mode",-1)==-1){
            SharedPreferences.Editor edit=setting.edit();
            edit.putInt("mode",0);
            edit.commit();
        }else{
            mode=setting.getInt("mode",0);
            System.out.println("mode"+mode);
        }


        list=new LinkedList<>();
        db=new DatabaseHAndler(this);


        if(mode==0){
            list=db.getTasks(0);
        }else if(mode==1){
            list=db.getTasks(1);
        }else{
            list=db.getTasks(2);
        }

        adapter=new RecyclerAdapter(this, list);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper touchHelper=new ItemTouchHelper(new RecyclerItems(adapter));
        touchHelper.attachToRecyclerView(recycler);



        JobScheduler();

        System.out.println("creater---------------------------------------------------------");
    }


    public void addTask(View view){

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.modaltask);


        final EditText input = dialog.findViewById(R.id.input);
        final Button btn1=dialog.findViewById(R.id.buttonadd);
        final Button btn2=dialog.findViewById(R.id.buttondate);

        btn1.setOnClickListener(e->{

            if((btn2.getText()).length()!=0 && input.getText().length()!=0){
            Tasks t=new Tasks(0,false,String.valueOf(input.getText()),String.valueOf(btn2.getText()));
            if(db.insertData(t)){
                Toast.makeText(this, "Амжилттай нэмэгдлээ !!!", Toast.LENGTH_SHORT).show();
                list.clear();
                list.addAll(db.getTasks(mode));
                recycler.setAdapter(adapter);
            }else{
                Toast.makeText(this, "Дахин оролдоно уу !!!", Toast.LENGTH_SHORT).show();
            }}
            else{
                Toast.makeText(this, "Талбарууд хоосон байна !!!", Toast.LENGTH_SHORT).show();
            }
        });
        btn2.setOnClickListener(e->{
            showDateTimeDialog(btn2);
        });
        dialog.show();

    }

    private void showDateTimeDialog(Button btn){
        Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        calendar.set(Calendar.HOUR_OF_DAY,i);
                        calendar.set(Calendar.MINUTE,i1);
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm");
                        btn.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(MainActivity.this, timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                        false).show();

            }
        };
        new DatePickerDialog(MainActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menu-g inflate hiine
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this,settings.class);
                startActivity(intent);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }


    public void JobScheduler(){
        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName serviceName = new ComponentName(getPackageName(), Notfication.class.getName());
        // iim job baival dahin vvsgeh shaardlaga baihgvi
        int i=-1;
        List<JobInfo> allPendingJobs = mScheduler.getAllPendingJobs();
        for (JobInfo job : allPendingJobs) {
            if (job.getId() == JOB_ID) {
                i=1;
            }
        }
        if(i==-1){
            JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName)
                    .setPeriodic(3600000);

            JobInfo myJobInfo = builder.build();
            mScheduler.schedule(myJobInfo);
        }
    }
}