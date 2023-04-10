package com.example.todoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.model.Tasks;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.zip.Inflater;

import Database.DatabaseHAndler;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{


    private LinkedList<Tasks>list;
    private LayoutInflater inflator;

    private DatabaseHAndler db;


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CheckBox task;
        TextView date;

        public ViewHolder(View item, RecyclerAdapter recyclerAdapter) {
            super(item);
            task=item.findViewById(R.id.check);
            date=item.findViewById(R.id.date);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public RecyclerAdapter(Context context, LinkedList<Tasks> tasks) {
        this.list=tasks;
        this.inflator=LayoutInflater.from(context);
        db=new DatabaseHAndler(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View task_item=inflator.inflate(R.layout.taskitems, parent, false);
        return new ViewHolder(task_item, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tasks current=list.get(position);
        holder.task.setText(current.getTask());
        holder.task.setChecked(current.getStatus());
        holder.date.setText(current.getDate());
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                current.setStatus(b);
                db.updateStatus(current);
            }
        });
    }
    Context getContext(){
        return inflator.getContext();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    void deleteItem(int position){
        db.deleteData(list.get(position));
        list.remove(position);
        notifyItemChanged(position);
    }

    public void editItem(int position) {
        final Dialog dialog = new Dialog(inflator.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.modaltask);

        final EditText input = dialog.findViewById(R.id.input);
        final Button btn1=dialog.findViewById(R.id.buttonadd);
        final Button btn2=dialog.findViewById(R.id.buttondate);
        btn2.setText(list.get(position).getDate());
        input.setText(list.get(position).getTask());
        btn1.setText("Өөрчлөх");

        btn1.setOnClickListener(e->{

            if((btn2.getText()).length()!=0 && input.getText().length()!=0){
                list.get(position).setDate(String.valueOf(btn2.getText()));
                list.get(position).setTask(String.valueOf(input.getText()));
                db.updateData(list.get(position));
                notifyItemChanged(position);
                Toast.makeText(inflator.getContext(), "Амжилттай өөрчиллөө !!!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(inflator.getContext(), "Талбарууд хоосон байна !!!", Toast.LENGTH_SHORT).show();
            }
        });
        btn2.setOnClickListener(e->{
            showDateTimeDialog(btn2);
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                notifyItemChanged(position);
            }
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

                new TimePickerDialog(inflator.getContext(), timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                        false).show();

            }
        };
        new DatePickerDialog(inflator.getContext(), dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


}
