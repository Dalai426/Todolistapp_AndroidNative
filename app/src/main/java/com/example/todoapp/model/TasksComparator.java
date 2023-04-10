package com.example.todoapp.model;


import android.os.Build;

import java.time.LocalDateTime;
import java.util.Comparator;

public class TasksComparator implements Comparator<Tasks> {

    @Override
    public int compare(Tasks tasks, Tasks t1) {
        return tasks.getDate().compareTo(t1.getDate());
    }
}
