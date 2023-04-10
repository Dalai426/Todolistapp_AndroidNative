package Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapp.model.Tasks;

import java.util.ArrayList;
import java.util.LinkedList;

public class DatabaseHAndler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="task";
    public static final int VER=1;
    public static final String TABLE_NAME="tasks";
    public static final String TASK="task";
    public static final String DATE="date";
    public static final String STATUS="status";
    public static final String KEY_ID="id";



    public DatabaseHAndler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VER);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create="create table "+TABLE_NAME+"("+TASK+" text, "+DATE+" text,"+STATUS+" integer,"+KEY_ID+" integer primary key autoincrement);";
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String upgrade="drop table if exists "+TABLE_NAME+";";
        sqLiteDatabase.execSQL(upgrade);
    }

    public boolean insertData(Tasks task){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TASK,task.getTask());
        values.put(DATE,task.getDate());
        values.put(STATUS,toBool(task.getStatus()));
        long result=db.insert(TABLE_NAME,null,values);
        db.close();
        if(result==-1){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean updateData(Tasks task){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TASK,task.getTask());
        values.put(DATE, task.getDate());
        values.put(STATUS,toBool(task.getStatus()));
        Cursor cursor=db.rawQuery("select * from "+TABLE_NAME+" where "+KEY_ID+"=?",new String[]{String.valueOf(task.getId())});
        if(cursor.getCount()>0){
            long result=db.update(TABLE_NAME,values,KEY_ID+"=?", new String[]{String.valueOf(task.getId())});
            db.close();
            if(result==-1){
                return false;
            }
            else{
                return true;
            }
        }else{
            db.close();
            return false;
        }
    }

    public boolean updateStatus(Tasks task){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(STATUS,toBool(task.getStatus()));
        Cursor cursor=db.rawQuery("select * from "+TABLE_NAME+" where "+KEY_ID+"=?",new String[]{String.valueOf(task.getId())});
        if(cursor.getCount()>0){
            long result=db.update(TABLE_NAME,values,KEY_ID+"=?", new String[]{String.valueOf(task.getId())});
            db.close();
            if(result==-1){
                return false;
            }
            else{
                return true;
            }
        }else{
            db.close();
            return false;
        }
    }

    public boolean deleteData(Tasks task){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+TABLE_NAME+" where "+KEY_ID+"=?",new String[]{String.valueOf(task.getId())});
        if(cursor.getCount()>0){
            long result=db.delete(TABLE_NAME, KEY_ID+"=?", new String[]{ String.valueOf(task.getId())});
            db.close();
            if(result==-1){
                return false;
            }
            else{
                return true;
            }
        }else{
            db.close();
            return false;
        }
    }

    public LinkedList<Tasks> getTasks(int mode){
        LinkedList<Tasks> array=new LinkedList<>();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+TABLE_NAME,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                System.out.println("hii");
                Tasks t=new Tasks(-1,false,"","");
                t.setTask(cursor.getString(0));
                t.setDate(cursor.getString(1));
                t.setStatus(toBool(cursor.getInt(2)));
                t.setId(cursor.getInt(3));

                if(mode==0) {
                    array.add(t);
                }else if(mode==1){
                    if(t.getStatus()==false){
                        array.add(t);
                    }
                }else{
                    if(t.getStatus()==true){
                        array.add(t);
                    }
                }
            }
        }else{
            db.close();
            return array;
        }
        db.close();
        return array;
    }

    boolean toBool(int a){
        if(a<0){
            return false;
        }else{
            return true;
        }
    }
    int toBool(boolean a){
        if(a){
            return 1;
        }else{
            return -1;
        }
    }


}
