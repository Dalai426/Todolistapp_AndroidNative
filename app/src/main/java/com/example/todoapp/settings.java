package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class settings extends AppCompatActivity {


    SharedPreferences setting;
    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setting=getSharedPreferences("todo", Context.MODE_PRIVATE);
        mode=setting.getInt("mode",0);

        switch (mode){
            case 0:
                ((RadioButton)findViewById(R.id.radioButton1)).setChecked(true);
                break;
            case 2:
                ((RadioButton)findViewById(R.id.radioButton2)).setChecked(true);
                break;
            default:
                ((RadioButton)findViewById(R.id.radioButton3)).setChecked(true);
                break;
        }

    }
    public void rb(View view){
        boolean checked=((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.radioButton1:
                if(checked)
                    mode=0;
                break;
            case R.id.radioButton2:
                if(checked)
                    mode=2;
                break;
            case R.id.radioButton3:
                if(checked)
                    mode=1;
                break;
        }
        SharedPreferences.Editor editor=setting.edit();
        editor.putInt("mode",mode);
        editor.apply();
    }
    public void save(View view){
        SharedPreferences.Editor editor=setting.edit();
        editor.putInt("mode",mode);
        editor.apply();
        Toast.makeText(this, "Mode "+mode, Toast.LENGTH_SHORT).show();
    }
    public void back(View view){
        Intent i=new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

}